package com.example.playqueue.data.models

import com.example.playqueue.data.GameSource
import com.example.playqueue.data.database.GameEntity
import kotlin.time.Clock

/**
 * A model to represent the BGG search detail response.
 */
data class BggGameDetails(
    val bggId: Int,
    val name: String,
    val description: String,
    val imageURL: String?,
    val thumbnailURL: String?,
    val minPlayer: Int?,
    val maxPlayer: Int?,
    val playingTimeInMinutes: Int?,
    val category: String?
)

/**
 * An extension on the BggGameDetails class that converts it to a GameEntity.
 */
fun BggGameDetails.createEntity(): GameEntity =
    GameEntity(
        title = name,
        description = description,
        imageURL = imageURL,
        thumbnailURL = thumbnailURL,
        minPlayers = minPlayer ?: 1,
        maxPlayers = maxPlayer ?: 1,
        playTimeMinutes = playingTimeInMinutes ?: 1,
        category = category ?: "",
        bggID = bggId,
        source = GameSource.API,
        dateAdded = Clock.System.now().toEpochMilliseconds()
    )
