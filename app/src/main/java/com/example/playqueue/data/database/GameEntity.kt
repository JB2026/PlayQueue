package com.example.playqueue.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playqueue.data.GameSource

/**
 * A data class that represents the main board game entity used in the Room database.
 */
@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String?,
    val imageURL: String?,
    val thumbnailURL: String?,
    val minPlayers: Int?,
    val maxPlayers: Int?,
    val minPlayTimeMinutes: Int?,
    val maxPlayTimeMinutes: Int?,
    val category: String?,
    val bggID: Int?,
    val source: GameSource,
    val dateAdded: Long
)