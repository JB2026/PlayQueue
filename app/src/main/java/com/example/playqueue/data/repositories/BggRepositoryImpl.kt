package com.example.playqueue.data.repositories

import com.example.playqueue.data.models.BggException
import com.example.playqueue.data.models.BggGameDetails
import com.example.playqueue.data.models.BggNetworkException
import com.example.playqueue.data.models.BggSearchResult
import com.example.playqueue.data.network.BggAPIService
import com.example.playqueue.data.network.BggXMLParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * An interface for the search repository defining all main methods accessible methods.
 */
interface BggRepository {
    /**
     * A method to get all games matching the search term from the BGG API.
     * @return A list of game search results that match the search term wrapped in a Flow.
     */
    fun searchBggAPI(searchTerm: String): Flow<List<BggSearchResult>>

    /**
     * A method to get a specific game from the BGG API.
     * @return The specific game if found that matches the ID wrapped in a Flow.
     */
    fun getGame(bggID: Int): Flow<BggGameDetails>
}

/**
 * A class representing the repository implementation conforming to the BggRepository with the BggAPIService and BggXMLParser injected.
 */
class BggRepositoryImpl @Inject constructor(
    private val bggAPIService: BggAPIService,
    private val xmlParser: BggXMLParser
): BggRepository {
    override fun searchBggAPI(searchTerm: String): Flow<List<BggSearchResult>> = flow {
        try {
            val gameSearchResponse = bggAPIService.search(searchTerm)

            if (gameSearchResponse.isSuccessful) {
                val gameSearchXML = gameSearchResponse.body()

                // If games are null or blank
                if (gameSearchXML.isNullOrBlank()) {
                    throw BggNetworkException("Invalid games returned for $searchTerm")
                } else {
                    emit(xmlParser.parseSearchResult(gameSearchXML))
                }
            } else {
                throw BggNetworkException("Failed to get search results for $searchTerm")
            }
        } catch (e: Exception) {
            // Send all errors received through the error state with the error message.
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    override fun getGame(bggID: Int): Flow<BggGameDetails> = flow {
        try {
            val gameResponse = bggAPIService.getGame(bggID)

            if (gameResponse.isSuccessful) {
                val gameXML = gameResponse.body()

                // If the game is null or blank
                if (gameXML.isNullOrBlank()) {
                    throw BggNetworkException("Invalid game returned for $bggID")
                } else {
                    emit(xmlParser.parseGameDetails(gameXML))
                }
            } else {
                throw BggNetworkException("Failed to get game for $bggID")
            }
        } catch (e: Exception) {
            // Send all errors received through the error state with the error message.
            throw BggException("Failed to get game for $bggID with error: $e")
        }
    }.flowOn(Dispatchers.IO)
}