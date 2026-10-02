package com.example.playqueue.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playqueue.data.GameStatus

/**
 * A data class that represents a board game copy entity used in the Room database.
 */
@Entity(tableName = "game_copies")
data class GameCopyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val gameID: Long,
    val copyNumber: Int,
    val status: GameStatus,
    val dateAdded: Long,
    val notes: String?
)
