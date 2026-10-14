package com.example.playqueue.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playqueue.data.database.GameEntity
import com.example.playqueue.data.database.updatedCopy
import com.example.playqueue.data.repositories.GameRepository
import com.example.playqueue.ui.models.GameUpdate
import com.example.playqueue.ui.models.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The view model for the Add/Edit Screen that is annotated as a HiltViewModel and conforms to ViewModel.
 * @param gameRepository The Game Repository.
 * @param savedStateHandle The saved state from the navigation transition.
 */
@HiltViewModel
class AddEditGameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val isEditing: Boolean = checkNotNull(savedStateHandle["isEditing"])
    val gameID: Long = checkNotNull(savedStateHandle["id"])
    private val _uiState = MutableStateFlow<UIState<GameEntity?>>(UIState.Loading)
    val uiState: StateFlow<UIState<GameEntity?>> = _uiState.asStateFlow()

    init {
        getGame()
    }

    fun saveGame(gameUpdate: GameUpdate) {
        if (isEditing) {
            viewModelScope.launch {
                gameRepository.getGame(gameID).collect {
                    gameRepository.updateGame(
                        it.updatedCopy(
                            gameUpdate.title,
                            gameUpdate.description,
                            gameUpdate.minPlayers,
                            gameUpdate.maxPlayers,
                            gameUpdate.playtimeMinutes,
                            gameUpdate.category
                        )
                    )
                }
            }
        } else {
            viewModelScope.launch {
                gameRepository.addManualGame(
                    gameUpdate.title,
                    gameUpdate.description,
                    gameUpdate.minPlayers,
                    gameUpdate.maxPlayers,
                    gameUpdate.playtimeMinutes,
                    gameUpdate.category
                )
            }
        }
    }

    private fun getGame() {
        if (isEditing) {
            viewModelScope.launch {
                gameRepository.getGame(gameID).collect {
                    _uiState.value = UIState.Success(it)
                }
            }
        } else {
            _uiState.value = UIState.Success(null)
        }
    }
}