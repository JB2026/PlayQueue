package com.example.playqueue.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.playqueue.ui.screens.InventoryScreen
import com.example.playqueue.ui.screens.ReservationsScreen
import com.example.playqueue.ui.screens.SearchScreen
import com.example.playqueue.ui.screens.TrackerScreen

/**
 * A composable navigation host that handles all the navigation in the application.
 */
@Composable
fun PlayQueueNavHost(
    navController: NavHostController,
    startDestination: NavigationDestination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = startDestination.route,
        modifier
    ) {
        NavigationDestination.entries.forEach { destination ->
            // Main App Tabs
            composable(destination.route) {
                when (destination) {
                    NavigationDestination.INVENTORY -> InventoryScreen()
                    NavigationDestination.TRACKER -> TrackerScreen()
                    NavigationDestination.RESERVATIONS -> ReservationsScreen()
                    NavigationDestination.SEARCH -> SearchScreen()
                }
            }
        }
    }
}