package com.example.playqueue.data.models

import com.example.playqueue.data.database.GameCopyEntity
import com.example.playqueue.data.database.GameEntity

data class GameAndCopies(
    val game: GameEntity,
    val copies: List<GameCopyEntity>
)
