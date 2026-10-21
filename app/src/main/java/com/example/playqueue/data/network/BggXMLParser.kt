package com.example.playqueue.data.network

import androidx.core.text.HtmlCompat
import com.example.playqueue.data.GameSource
import com.example.playqueue.data.models.BggGameDetails
import com.example.playqueue.data.models.BggParsingException
import com.example.playqueue.data.models.BggSearchResult
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader

/**
 * Converts the BGG XML Response into an object.
 */
class BggXMLParser {

    @Throws(BggParsingException::class)
    fun parseSearchResult(xml: String): List<BggSearchResult> {
        val searchResults = mutableListOf<BggSearchResult>()

        var currentID: Int? = null
        var currentName: String? = null
        var currentNameIsPrimary = false
        var currentYear: Int? = null

        try {
            val parser = createParser(xml)
            var event = parser.eventType

            while (event != XmlPullParser.END_DOCUMENT) {
                when (event) {
                    XmlPullParser.START_TAG -> when (parser.name) {
                        "item" -> {
                            currentID = parser.attr("id")?.toIntOrNull()
                            currentName = null
                            currentNameIsPrimary = false
                            currentYear = null
                        }

                        "name" -> {
                            val value = parser.attr("value")
                            val isPrimary = parser.attr("type") == "primary"

                            // Always take a primary name if in the XML document
                            if (currentName == null || (isPrimary && !currentNameIsPrimary)) {
                                currentName = value
                                currentNameIsPrimary = isPrimary
                            }
                        }

                        "yearpublished" -> currentYear = parser.attr("value")?.toIntOrNull()
                    }

                    XmlPullParser.END_TAG -> if (parser.name == "item") {
                        val id = currentID
                        val name = currentName

                        if (id != null && name != null) {
                            searchResults.add(
                                BggSearchResult(
                                    id,
                                    null,
                                    name,
                                    currentYear,
                                    gameSource = GameSource.API
                                )
                            )
                        }
                        currentID = null
                        currentName = null
                    }
                }

                event = parser.next()
            }
        } catch (e: XmlPullParserException) {
            throw BggParsingException("Failed to parse search results", e)
        } catch (e: IOException) {
            throw BggParsingException("", e)
        }

        return searchResults
    }

    @Throws(BggParsingException::class)
    fun parseGameDetails(xml: String): BggGameDetails {
        var foundGame = false
        var bggID: Int? = null
        var name: String? = null
        var nameIsPrimary = false
        var description = ""
        var imageURL: String? = null
        var thumbnailURL: String? = null
        var minPlayers: Int? = null
        var maxPlayers: Int? = null
        var playtimeInMinutes: Int? = null
        val categories = mutableListOf<String>()

        try {
            val parser = createParser(xml)
            var event = parser.eventType

            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    when (parser.name) {
                        "item" -> {
                            if (!foundGame) {
                                foundGame = true
                                bggID = parser.attr("id").toIntOrNull()
                            }
                        }

                        "name" -> {
                            val value = parser.attr("value")
                            val isPrimary = parser.attr("type") == "primary"

                            if (name == null || (isPrimary && !nameIsPrimary)) {
                                name = value
                                nameIsPrimary = isPrimary
                            }
                        }

                        "description" -> description = decodeDescription(parser.nextText())
                        "image" -> imageURL = parser.nextText().trim().ifBlank { null }
                        "thumbnail" -> thumbnailURL = parser.nextText().trim().ifBlank { null }

                        "minplayers" -> minPlayers = parser.attr("value").toIntOrNull()
                        "maxplayers" -> maxPlayers = parser.attr("value").toIntOrNull()
                        "playingtime" -> playtimeInMinutes = parser.attr("value").toIntOrNull()

                        "link" -> {
                            val value = parser.attr("value")
                            if (value != null) {
                                when (parser.attr("type")) {
                                    "boardgamecategory" -> categories.add(value)
                                }
                            }
                        }
                    }
                }
                event = parser.next()
            }
        } catch (e: XmlPullParserException) {
            throw BggParsingException("Could not parse BGG Game Details", e)
        } catch (e: IOException) {
            throw BggParsingException("Could not read BGG Game Details", e)
        }

        if (!foundGame) throw BggParsingException("No game found with that ID")
        val id = bggID ?: throw BggParsingException("Game Details missing ID")
        val title = name ?: throw BggParsingException("Game Details missing game name")
        
        return BggGameDetails(
            bggId = id,
            name = title,
            description = description,
            imageURL = imageURL,
            thumbnailURL = thumbnailURL,
            minPlayer = minPlayers,
            maxPlayer = maxPlayers,
            playingTimeInMinutes = playtimeInMinutes,
            category = categories.joinToString(separator = ", ")
        )
    }

    @Throws(BggParsingException::class)
    private fun createParser(xml: String): XmlPullParser =
        try {
            XmlPullParserFactory.newInstance().newPullParser().apply {
                setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                setInput(StringReader(xml))
            }
        } catch (e: XmlPullParserException) {
            throw BggParsingException("Failed to parse the response", e)
        }

    private fun XmlPullParser.attr(name: String): String = getAttributeValue(null, name)

    private fun decodeDescription(raw: String): String {
        val withBreaks = raw
            .replace("\r\n", "\n")
            .replace("\n", "<br>")
            .replace("&#10;", "<br>")   // in case BGG sends encoded newlines
        return HtmlCompat.fromHtml(withBreaks, HtmlCompat.FROM_HTML_MODE_LEGACY)
            .toString()
            .trim()
    }
}