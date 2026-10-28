package com.example.playqueue.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.playqueue.R
import com.example.playqueue.data.database.GameEntity
import com.example.playqueue.ui.models.UIState
import com.example.playqueue.ui.shared.EmptyView
import com.example.playqueue.ui.shared.ErrorView
import com.example.playqueue.ui.shared.LoadingView
import com.example.playqueue.viewModels.InventoryViewModel

@Composable
fun EmptyView() {
    Column {
        Spacer(modifier = Modifier.height(30.dp))
    }
}

/**
 * A composable for the inventory screen.
 * @param onEditSelect A closure to call to edit a game.
 * @param onAddSelect The closure to call when adding a game.
 * @param onItemSelect The closure to call when selecting a game.
 * @param inventoryViewModel The InventoryViewModel view model.
 */
@Composable
fun InventoryScreen(
    onEditSelect: (GameEntity) -> Unit,
    onAddSelect: () -> Unit,
    onItemSelect: (GameEntity) -> Unit,
    inventoryViewModel: InventoryViewModel = hiltViewModel()
) {
    val uiState by inventoryViewModel.uiState.collectAsStateWithLifecycle()
    AnimatedContent(
        targetState = uiState,
        label = "StateTransition"
    ) { state ->
        when (state) {
            is UIState.Loading -> LoadingView()
            is UIState.Error -> ErrorView()
            is UIState.Success -> InventoryList(state.data, onEditSelect,onAddSelect, onItemSelect)
        }
    }
}

/**
 * A composable for the game list.
 * @param onEditSelect A closure to call to edit a game.
 * @param onAddSelect The closure to call when adding a game.
 * @param onItemSelect The closure to call when selecting a game.
 * @param data The list of games.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryList(
    data: List<GameEntity>,
    onEditSelect: (GameEntity) -> Unit,
    onAddSelect: () -> Unit,
    onItemSelect: (GameEntity) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar({ Text("Inventory")} ) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddSelect() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        // Create the list using the inner padding of the Scaffold
        if (data.isNotEmpty()) {
            LazyColumn(contentPadding = padding) {
                itemsIndexed(data) { index, game ->
                    GameOverviewView(game = game, onItemSelect, onEditSelect)
                    if (index < data.lastIndex) {
                        HorizontalDivider()
                    }
                }
            }
        } else {
            // If the list is empty show the empty view.
            EmptyView("There is nothing in the inventory.")
        }
    }
}

/**
 * A composable for the game overview.
 * @param game The game to display.
 * @param onEditSelect The closure to call when editing a game.
 * @param onItemSelect The closure to call when selecting a game.
 */
@Composable
fun GameOverviewView(
    game: GameEntity,
    onItemSelect: (GameEntity) -> Unit,
    onEditSelect: (GameEntity) -> Unit,
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ListItem(
            headlineContent = {
                Text(
                    game.title,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            leadingContent = {
                AsyncImage(
                    model = game.thumbnailURL,
                    placeholder = painterResource(R.drawable.image_placeholder),
                    error = painterResource(R.drawable.image_error),
                    contentDescription = "${game.title} image",
                    modifier = Modifier.size(60.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            },
            trailingContent = {
                IconButton(onClick = { onEditSelect(game) }) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
            },
            modifier = Modifier.clickable {
                onItemSelect(game)
            }.testTag("inventory game")
        )
    }
}