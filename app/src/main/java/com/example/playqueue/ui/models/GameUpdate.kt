package com.example.playqueue.ui.models

data class GameUpdate(
    val title: String,
    val description: String,
    val minPlayers: Int,
    val maxPlayers: Int,
    val playtimeMinutes: Int,
    val category: String
)
