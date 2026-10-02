package com.example.playqueue.data.repositories

import com.example.playqueue.data.GameSource
import com.example.playqueue.data.GameStatus
import com.example.playqueue.data.database.GameCopyDao
import com.example.playqueue.data.database.GameCopyEntity
import com.example.playqueue.data.database.GameDao
import com.example.playqueue.data.database.GameEntity
import com.example.playqueue.data.database.ReservationDao
import com.example.playqueue.data.models.GameAndCopies
import com.example.playqueue.data.models.GameDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import kotlin.time.Clock

interface GameRepository {
    fun addGameFromRemote(details: GameDetails): Long
    suspend fun addManualGame(title: String, description: String, minPlayers: Int, maxPlayers: Int, playtimeMinutes: Int): Long
    suspend fun updateGame(game: GameEntity)
    suspend fun deleteGame(gameID: Long): Result<Unit>
    fun getAllGames(): Flow<List<GameEntity>>
    fun searchCatalog(query: String): Flow<List<GameEntity>>
    fun getGameCopies(gameID: Long): Flow<GameAndCopies>
    suspend fun addCopy(gameID: Long, copyNumber: Int): Long
}

class GameRepositoryImpl @Inject constructor(
    private val gameDao: GameDao,
    private val gameCopyDao: GameCopyDao,
    private val reservationDao: ReservationDao
): GameRepository {

    override fun addGameFromRemote(details: GameDetails): Long {
        TODO("Not yet implemented")
    }

    override suspend fun addManualGame(
        title: String,
        description: String,
        minPlayers: Int,
        maxPlayers: Int,
        playtimeMinutes: Int
    ): Long {
        val game = GameEntity(
            title = title,
            minPlayers = minPlayers,
            maxPlayers = maxPlayers,
            minPlayTimeMinutes = playtimeMinutes,
            maxPlayTimeMinutes = playtimeMinutes,
            source = GameSource.MANUAL,
            dateAdded = Clock.System.now().toEpochMilliseconds(),
            imageURL = null,
            thumbnailURL = null,
            category = null,
            bggID = null
        )

        return gameDao.insert(game)
    }

    override suspend fun updateGame(game: GameEntity) {
        gameDao.update(game)
    }

    override suspend fun deleteGame(gameID: Long): Result<Unit> {
        val copiesCount = gameCopyDao.getMaxCopyNumber(gameID)
        val reservationCount = reservationDao.getMatchingReservations(gameID).count()
        val game = gameDao.getGame(gameID).firstOrNull()

        game?.let { game ->
            if (copiesCount == 0 && reservationCount == 0) {
                gameDao.delete(game)
                return Result.success(Unit)
            } else {
                return Result.failure(Exception("Game still has reservation or copies"))
            }
        }

        return Result.failure(Exception("Matching game not found."))
    }

    override fun getAllGames(): Flow<List<GameEntity>> {
        return gameDao.getAllGames()
    }

    override fun searchCatalog(query: String): Flow<List<GameEntity>> {
        return gameDao.searchGamesByTitle(query)
    }

    override fun getGameCopies(gameID: Long): Flow<GameAndCopies> =
        combine(
            gameDao.getGame(gameID),
            gameCopyDao.getAllCopies(gameID)
        ) { game, gameCopies ->
            game?.let {
                GameAndCopies(game, gameCopies)
            }
        }.filterNotNull()

    override suspend fun addCopy(gameID: Long, copyNumber: Int): Long {
        val maxCopyNumber = gameCopyDao.getMaxCopyNumber(gameID)
        val gameCopy = GameCopyEntity(
            gameID = gameID,
            copyNumber = maxCopyNumber + 1,
            status = GameStatus.AVAILABLE,
            dateAdded = Clock.System.now().toEpochMilliseconds(),
            notes = ""
        )
       return gameCopyDao.insert(gameCopy)
    }
}