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
    val title: String,
    val description: String,
    val imageURL: String?,
    val thumbnailURL: String?,
    val minPlayers: Int,
    val maxPlayers: Int,
    val minPlayTimeMinutes: Int?,
    val maxPlayTimeMinutes: Int,
    val category: String,
    val bggID: Int?,
    val source: GameSource,
    val dateAdded: Long
)

/**
 * An extension on the GameEntity class that creates a copy with update fields.
 */
fun GameEntity.updatedCopy(
    title: String,
    description: String,
    minPlayers: Int,
    maxPlayers: Int,
    playtimeMinutes: Int,
    category: String
): GameEntity {
    return GameEntity(
        id = this.id,
        title = title,
        description = description,
        imageURL = this.imageURL,
        thumbnailURL = this.thumbnailURL,
        minPlayers = minPlayers,
        maxPlayers = maxPlayers,
        minPlayTimeMinutes = playtimeMinutes,
        maxPlayTimeMinutes = playtimeMinutes,
        category = category,
        bggID = this.bggID,
        source = this.source,
        dateAdded = this.dateAdded
    )
}