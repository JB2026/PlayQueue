package com.example.playqueue.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.playqueue.data.models.BggSearchResult
import com.example.playqueue.ui.shared.EmptyView
import com.example.playqueue.viewModels.SearchViewModel

/**
 * A composable for the search screen.
 * @param searchViewModel The search view model.
 * @param onItemSelect The closure to call when selecting an item.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    onItemSelect: (BggSearchResult) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults by searchViewModel.searchResults.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar({ Text("Search")} ) },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { newTerm ->
                    searchQuery = newTerm
                    searchViewModel.updateSearchTerm(newTerm)
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).testTag("search field"),
                label = { Text("Search Games...") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                }
            )

            if (searchQuery.isBlank()) {
                // Show the empty screen when no search results are returned.
                EmptyView("Search games by entering a game title!")
            } else if (searchResults.isNotEmpty()) {
                LazyColumn {
                    itemsIndexed(searchResults) { index, game ->
                        GameSearchItemOverviewView(gameSearchResult = game, onItemSelect)
                        if (index < searchResults.lastIndex) {
                            HorizontalDivider()
                        }
                    }
                }
            } else {
                // Show the empty screen when no search results are returned.
                EmptyView("Sorry, no games match that search!")
            }
        }
    }
}

/**
 * A composable for the game search overview row.
 * @param gameSearchResult The game search result model.
 * @param onItemSelect The closure to call when selecting an item.
 */
@Composable
fun GameSearchItemOverviewView(
    gameSearchResult: BggSearchResult,
    onItemSelect: (BggSearchResult) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ListItem(
            headlineContent = {
                Text(
                    gameSearchResult.name,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            supportingContent = {
                Text(
                    "Published: ${gameSearchResult.publishYear.toString()}",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            modifier = Modifier.clickable {
                onItemSelect(gameSearchResult)
            }.testTag("search item")
        )
    }
}