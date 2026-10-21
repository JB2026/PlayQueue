package com.example.playqueue.data.models

import com.example.playqueue.data.GameSource

/**
 * A model to represent the BGG search result response.
 */
data class BggSearchResult(
    val bggID: Int?,
    val databaseID: Long?,
    val name: String,
    val publishYear: Int?,
    val gameSource: GameSource
)
