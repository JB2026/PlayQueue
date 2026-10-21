package com.example.playqueue.data.repositories

import com.example.playqueue.data.database.GameDao
import com.example.playqueue.data.database.GameEntity
import com.example.playqueue.data.models.BggSearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * An interface for the search repository defining all main methods accessible methods.
 */
interface SearchRepository {
    /**
     * A method to get all games matching the search term.
     * @param searchTerm The game name to look for
     * @return A list of game search results that match the search term wrapped in a Flow.
     */
    fun searchBggAPI(searchTerm: String): Flow<List<BggSearchResult>>

    /**
     * A method to get a specific game from the BGG API if it exists.
     * @param searchTerm The game name to look for
     * @return A nullable game that has the matching ID.
     */
    fun searchStoredGames(searchTerm: String): Flow<List<GameEntity>>
}

/**
 * A class representing the repository implementation conforming to the SearchRepository with the GameDao and BggAPIService are injected.
 */
class SearchRepositoryImpl @Inject constructor(
    private val gameDao: GameDao,
    private val bggRepository: BggRepository
): SearchRepository {

    override fun searchBggAPI(searchTerm: String): Flow<List<BggSearchResult>> =
        bggRepository.searchBggAPI(searchTerm).flowOn(Dispatchers.IO)

    override fun searchStoredGames(searchTerm: String): Flow<List<GameEntity>> =
        gameDao.searchGamesByTitle(searchTerm)
}