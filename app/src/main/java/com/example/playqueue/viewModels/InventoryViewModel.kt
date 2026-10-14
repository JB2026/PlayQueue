package com.example.playqueue.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playqueue.data.database.GameEntity
import com.example.playqueue.data.repositories.GameRepository
import com.example.playqueue.ui.models.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The view model for the Inventory Screen that is annotated as a HiltViewModel and conforms to ViewModel.
 * @param gameRepository The Game Repository.
 */
@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val gameRepository: GameRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<UIState<List<GameEntity>>>(UIState.Loading)
    val uiState: StateFlow<UIState<List<GameEntity>>> = _uiState.asStateFlow()

    init {
        fetchGames()
    }

    private fun fetchGames() {
        viewModelScope.launch {
            gameRepository.getAllGames().collect {
                _uiState.value = UIState.Success(it)
            }
        }
    }
}