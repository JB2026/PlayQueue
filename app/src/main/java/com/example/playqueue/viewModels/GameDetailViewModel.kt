package com.example.playqueue.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playqueue.data.database.GameEntity
import com.example.playqueue.data.models.GameAndCopies
import com.example.playqueue.data.models.createEntity
import com.example.playqueue.data.repositories.BggRepository
import com.example.playqueue.data.repositories.GameRepository
import com.example.playqueue.ui.models.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The view model for the Game Detail Screen that is annotated as a HiltViewModel and conforms to ViewModel.
 * @param gameRepository The Game Repository.
 */
@HiltViewModel
class GameDetailViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val bggRepository: BggRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private var gameDatabaseID: Long = checkNotNull(savedStateHandle["databaseID"])
    private val gameBggID: Int = checkNotNull(savedStateHandle["bggID"])
    private val isManual: Boolean = checkNotNull(savedStateHandle["isManual"])

    private val _uiState = MutableStateFlow<UIState<GameAndCopies>>(UIState.Loading)
    val uiState: StateFlow<UIState<GameAndCopies>> = _uiState.asStateFlow()

    val isInInventory: Boolean
        get() = gameDatabaseID != -1L

    init {
        fetchGameAndCopies()
    }

    fun addGame(game: GameEntity) {
        viewModelScope.launch {
            gameDatabaseID = gameRepository.addGame(game)
            fetchGameAndCopies()
        }
    }

    fun removeGame(game: GameEntity, onRemove: () -> Unit) {
        viewModelScope.launch {
            gameRepository.deleteGame(game.id)
                .onSuccess {
                    onRemove()
                }
                .onFailure {
                    print("Failed to delete")
                //TODO: Throw or send something
                }
        }
    }

    fun addCopy(game: GameEntity) {
        viewModelScope.launch {
            gameRepository.addCopy(game.id)
            fetchGameAndCopies()
        }
    }

    fun removeCopy(game: GameEntity) {
        viewModelScope.launch {
            gameRepository.removeCopy(game.id)
            fetchGameAndCopies()
        }
    }

    private fun fetchGameAndCopies() {
        viewModelScope.launch {
            if (gameDatabaseID != -1L) {
                gameRepository.getGameCopies(gameDatabaseID).collect {
                    _uiState.value = UIState.Success(it)
                }
            } else if (gameBggID != -1) {
                bggRepository.getGame(bggID = gameBggID).collect {
                    val gameAndCopies = GameAndCopies(game = it.createEntity(), copies = emptyList())
                    _uiState.value = UIState.Success(gameAndCopies)
                }
            }
        }
    }
}
