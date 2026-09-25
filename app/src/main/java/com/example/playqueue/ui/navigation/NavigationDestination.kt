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