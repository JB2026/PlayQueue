package com.example.playqueue.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.playqueue.R
import com.example.playqueue.data.models.GameAndCopies
import com.example.playqueue.ui.models.UIState
import com.example.playqueue.ui.shared.ErrorView
import com.example.playqueue.ui.shared.LoadingView
import com.example.playqueue.viewModels.GameDetailViewModel

/**
 * A composable for the game detail screen.
 * @param gameDetailViewModel The game detail view model.
 */
@Composable
fun GameDetailScreen(
    gameDetailViewModel: GameDetailViewModel = hiltViewModel(),
    onClose: () -> Unit,
) {
    val uiState by gameDetailViewModel.uiState.collectAsStateWithLifecycle()
    AnimatedContent(
        targetState = uiState,
        label = "StateTransition"
    ) { state ->
        when (state) {
            is UIState.Loading -> LoadingView()
            is UIState.Error -> ErrorView()
            is UIState.Success -> GameDetailColumn(
                state.data,
                gameDetailViewModel,
                onClose = onClose
            )
        }
    }
}

/**
 * A composable for the game detail view.
 */
@Composable
fun GameDetailColumn(gameData: GameAndCopies,
                     viewModel: GameDetailViewModel,
                     onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(
                MaterialTheme.colorScheme.surface
            )
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Box {
                    AsyncImage(
                        model = gameData.game.imageURL,
                        placeholder = painterResource(R.drawable.image_placeholder),
                        error = painterResource(R.drawable.image_error),
                        contentDescription = "${gameData.game.title} image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(top = 8.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            item {
                Text(
                    gameData.game.title,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "# of Players",
                            style = MaterialTheme.typography.titleSmall,
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            "${gameData.game.minPlayers} - ${gameData.game.maxPlayers}",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Playtime (min)",
                            style = MaterialTheme.typography.titleSmall,
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            "${gameData.game.playTimeMinutes}",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            }

            item {
                InventoryActions(
                    viewModel.isInInventory,
                    gameData.copies.size,
                    onAddGame = { viewModel.addGame(gameData.game) },
                    onRemoveGame = {
                        viewModel.removeGame(
                            gameData.game,
                            onRemove = onClose
                        )
                    },
                    onAddCopy = { viewModel.addCopy(gameData.game) },
                    onRemoveCopy = { viewModel.removeCopy(gameData.game)  }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            }

            item {
                Text(
                    "Description",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textDecoration = TextDecoration.Underline
                )
            }

            item {
                Text(
                    gameData.game.description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun InventoryActions(
    inInventory: Boolean,
    numberOfCopies: Int,
    onAddGame: () -> Unit,
    onRemoveGame: () -> Unit,
    onAddCopy: () -> Unit,
    onRemoveCopy: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(8.dp),
        Arrangement.spacedBy(16.dp)
    ) {
        AnimatedVisibility(visible = inInventory) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "# of Copies",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            "$numberOfCopies",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    FilledTonalIconButton(
                        onClick = onRemoveCopy,
                        enabled = numberOfCopies > 0
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Remove copy")
                    }
                    Spacer(Modifier.width(4.dp))
                    FilledTonalIconButton(
                        onClick = onAddCopy,
                        enabled = inInventory
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add copy")
                    }
                }
            }
        }

        Crossfade(targetState = inInventory, label = "addRemoveGame") { isInInventory ->
            if (isInInventory) {
                OutlinedButton(
                    onClick = onRemoveGame,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "delete")
                    Spacer(Modifier.width(8.dp))
                    Text("Remove from Inventory")
                }
            } else {
                Button(
                    onClick = onAddGame,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "add")
                    Spacer(Modifier.width(8.dp))
                    Text("Add to Inventory")
                }
            }

        }
    }
}
