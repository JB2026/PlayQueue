package com.example.playqueue.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.example.playqueue.data.GameSource
import com.example.playqueue.ui.screens.AddEditGameScreen
import com.example.playqueue.ui.screens.GameDetailScreen
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
                    NavigationDestination.INVENTORY -> InventoryScreen(
                        onEditSelect = { game ->
                            navController.navigate(
                                PlayQueueRoutes.GameEdit.createRoute(id = game.id, isEditing = true)
                            )
                        },
                        onAddSelect = {
                            navController.navigate(
                                PlayQueueRoutes.GameEdit.createRoute(isEditing = false)
                            )
                        },
                        onItemSelect = { game ->
                            navController.navigate(
                                PlayQueueRoutes.GameDetail.createRoute(game.id, bggID = game.bggID, isManual = game.source == GameSource.MANUAL)
                            )
                        }
                    )
                    NavigationDestination.TRACKER -> TrackerScreen()
                    NavigationDestination.RESERVATIONS -> ReservationsScreen()
                    NavigationDestination.SEARCH -> SearchScreen(
                        onItemSelect = { game ->
                            navController.navigate(
                                PlayQueueRoutes.GameDetail.createRoute(game.databaseID ?: -1L, game.bggID ?: -1, game.gameSource == GameSource.MANUAL)
                            )
                        }
                    )
                }
            }

            // The edit recipe screen
            composable(
                route = PlayQueueRoutes.GameEdit.route,
                arguments = listOf(
                    navArgument("id") { type = NavType.LongType },
                    navArgument("isEditing") { type = NavType.BoolType }
                )
            ) {
                AddEditGameScreen(onClose = { navController.popBackStack() })
            }

            // The game detail screen
            dialog(
                route = PlayQueueRoutes.GameDetail.route,
                arguments = listOf(
                    navArgument("databaseID") { type = NavType.LongType },
                    navArgument("bggID") { type = NavType.IntType },
                    navArgument("isManual") { type = NavType.BoolType }
                )
            ) {
                GameDetailScreen()
            }
        }
    }
}