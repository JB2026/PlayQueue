package com.example.playqueue.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playqueue.data.models.BggSearchResult
import com.example.playqueue.data.repositories.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

/**
 * The view model for the Search Screen that is annotated as a HiltViewModel and conforms to ViewModel.
 * @param searchRepository The Search Repository.
 */
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {
    private val searchQuery = MutableStateFlow("")

    val searchResults = searchQuery
        .debounce(300.milliseconds)
        .distinctUntilChanged()
        .filter { it.length >= 2 }
        .flatMapLatest { searchTerm ->
            val apiSearch = searchRepository.searchBggAPI(searchTerm)
            val storedGameSearch = searchRepository.searchStoredGames(searchTerm)

            // Combine all three sources of recipes
            combine(apiSearch, storedGameSearch) { apiSearch, storedGameSearch ->
                val storedSearchResults = storedGameSearch.map { BggSearchResult(
                    bggID = it.bggID,
                    databaseID = it.id,
                    name = it.title,
                    publishYear = null,
                    gameSource = it.source
                ) }
                (apiSearch + storedSearchResults).distinctBy { it.bggID }.sortedBy { it.name }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * A method to update the search query.
     * @param searchTerm The new search term.
     */
    fun updateSearchTerm(searchTerm: String) {
        searchQuery.value = searchTerm
    }
}