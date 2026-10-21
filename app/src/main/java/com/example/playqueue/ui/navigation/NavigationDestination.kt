package com.example.playqueue.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * An enum class that holds all the navigation destinations.
 */
enum class NavigationDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    INVENTORY("inventory", "Inventory", Icons.Default.Inventory, "Inventory"),
    TRACKER("tracker", "Tracker", Icons.Default.TrackChanges, "Game Tracker"),
    RESERVATIONS("reservations", "Reservations", Icons.Default.CalendarMonth, "Reservations"),
    SEARCH("search", "Search", Icons.Default.Search, "Search")
}

/**
 * A sealed class of possible routes throughout the application.
 */
sealed class PlayQueueRoutes(val route: String) {

    // The game detail screen
    object GameDetail: PlayQueueRoutes("detail/{databaseID}/{bggID}/{isManual}"){
        fun createRoute(databaseID: Long?, bggID: Int?, isManual: Boolean) = "detail/$databaseID/$bggID/$isManual"
    }

    // The edit game screen
    object GameEdit: PlayQueueRoutes("edit/{id}/{isEditing}"){
        fun createRoute(id: Long = -1, isEditing: Boolean) = "edit/$id/$isEditing"
    }
}