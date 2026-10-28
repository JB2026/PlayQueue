package com.example.playqueue.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.playqueue.data.database.GameEntity
import com.example.playqueue.ui.models.GameUpdate
import com.example.playqueue.ui.models.UIState
import com.example.playqueue.ui.shared.ErrorView
import com.example.playqueue.ui.shared.LoadingView
import com.example.playqueue.viewModels.AddEditGameViewModel

/**
 * A composable for the add/edit custom recipe screen.
 * @param onClose A closure to call to close the screen.
 * @param addEditGameViewModel The AddEditRecipeViewModel view model
 */
@Composable
fun AddEditGameScreen(
    onClose: () -> Unit,
    addEditGameViewModel: AddEditGameViewModel = hiltViewModel()
) {
    // Define properties for the main list and dialog as state values.
    val uiState by addEditGameViewModel.uiState.collectAsStateWithLifecycle()
    AnimatedContent(
        targetState = uiState,
        label = "StateTransition"
    ) { state ->
        when (state) {
            is UIState.Loading -> LoadingView()
            is UIState.Error -> ErrorView()
            is UIState.Success -> AddEditGameView(
                addEditGameViewModel.isEditing,
                state.data,
                onCreate = { addEditGameViewModel.saveGame(it) },
                onUpdate = { addEditGameViewModel.saveGame(it) },
                onClose = { onClose() }
            )
        }
    }
}

/**
 * A composable add/edit game view
 * @param isEditing True if the game was already created and this is an edit.
 * @param game The closure to call upon a measurement change.
 * @param onCreate The closure to call upon an ingredient name change.
 * @param onUpdate The closure to call upon an ingredient name change.
 * @param onClose The closure to call upon an ingredient name change.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditGameView(
    isEditing: Boolean,
    game: GameEntity?,
    onCreate: (GameUpdate) -> Unit,
    onUpdate: (GameUpdate) -> Unit,
    onClose: () -> Unit
) {
    var gameTitle by remember { mutableStateOf(game?.title ?: "") }
    var gameDescription by remember { mutableStateOf(game?.description ?: "") }
    var gameMinPlayers by remember { mutableIntStateOf(game?.minPlayers ?: 1) }
    var gameMaxPlayers by remember { mutableIntStateOf(game?.maxPlayers ?: 1) }
    var gamePlaytimeMinutes by remember { mutableIntStateOf(game?.playTimeMinutes ?: 1) }
    var gameCategory by remember { mutableStateOf(game?.category ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add / Edit Game") },
                navigationIcon = {
                    IconButton(onClick = { onClose() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {

                            val gameUpdate = GameUpdate(
                                gameTitle,
                                gameDescription,
                                gameMinPlayers,
                                gameMaxPlayers,
                                gamePlaytimeMinutes,
                                gameCategory
                            )
                            if (isEditing) {
                                onUpdate(gameUpdate)
                            } else {
                                onCreate(gameUpdate)
                            }

                            onClose()
                        }
                    ) {
                        Icon(
                            Icons.Default.Save,
                            contentDescription = "Save"
                        )
                    }
                }
            ) },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp)) {
                item {
                    OutlinedTextField(
                        value = gameTitle,
                        onValueChange = { gameTitle = it },
                        label = { Text("Game Title") },
                        modifier = Modifier.fillMaxWidth().padding(top= 8.dp).testTag("game title field")
                    )
                }

                item {
                    OutlinedTextField(
                        value = gameCategory,
                        onValueChange = { gameCategory = it },
                        label = { Text("Category") },
                        modifier = Modifier.fillMaxWidth().padding(top= 8.dp).testTag("game category field")
                    )
                }

                item {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                }

                item {
                    PlayerNumberPickerRow(
                        minPlayers = gameMinPlayers,
                        maxPlayers = gameMaxPlayers,
                        onMinChange = { gameMinPlayers = it },
                        onMaxChange = { gameMaxPlayers = it }
                    )
                }

                item {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                }

                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        LabeledNumberPicker(
                            label = "Playtime in Minutes",
                            value = gamePlaytimeMinutes,
                            maxValue = 480,
                            onNumberChange = { gamePlaytimeMinutes = it }
                        )
                    }
                }

                item {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                }

                item {
                    OutlinedTextField(
                        value = gameDescription,
                        onValueChange = { gameDescription = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth().height(200.dp).testTag("game description field")
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerNumberPickerRow (
    minPlayers: Int,
    maxPlayers: Int,
    onMinChange: (Int) -> Unit,
    onMaxChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LabeledNumberPicker(
            label = "Min # of Players",
            value = minPlayers,
            maxValue = 50,
            onNumberChange = { onMinChange(it) }
        )

        LabeledNumberPicker(
            label = "Max # of Players",
            value = maxPlayers,
            maxValue = 50,
            onNumberChange = { onMaxChange(it)}
        )
    }
}


@Composable
fun LabeledNumberPicker(
    label: String,
    value: Int,
    maxValue: Int,
    onNumberChange: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val textColor = MaterialTheme.colorScheme.onSurface.toArgb()

        AndroidView(
            factory = { context ->
                android.widget.NumberPicker(context).apply {
                    minValue = 1
                    this.maxValue = maxValue
                    this.value = value
                    this.applyCurrentThemeColor(textColor)

                    setOnValueChangedListener { _, _, newValue ->
                        onNumberChange(newValue)
                    }
                }
            },
            update = { numberPicker ->
                numberPicker.value = value
                numberPicker.applyCurrentThemeColor(textColor)
            }
        )
    }
}

private fun android.widget.NumberPicker.applyCurrentThemeColor(color: Int) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
        this.textColor = color
    } else {
        try {
            val paintField = android.widget.NumberPicker::class.java
                .getDeclaredField("mSelectorWheelPaint")
            paintField.isAccessible = true
            (paintField.get(this) as android.graphics.Paint).color = color
        } catch(e: Exception) {
            Log.e("NumberPickerError", "Could not set number picker color", e)
        }
    }
}