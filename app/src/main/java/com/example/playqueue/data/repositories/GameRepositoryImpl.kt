package com.example.playqueue.data.repositories

import com.example.playqueue.data.GameSource
import com.example.playqueue.data.GameStatus
import com.example.playqueue.data.database.GameCopyDao
import com.example.playqueue.data.database.GameCopyEntity
import com.example.playqueue.data.database.GameDao
import com.example.playqueue.data.database.GameEntity
import com.example.playqueue.data.database.ReservationDao
import com.example.playqueue.data.models.BggGameDetails
import com.example.playqueue.data.models.GameAndCopies
import com.example.playqueue.data.models.createEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import kotlin.time.Clock

/**
 * An interface for the game repository defining all main methods accessible methods.
 */
interface GameRepository {
    suspend fun addGameFromRemote(details: BggGameDetails): Long

    suspend fun addGame(game: GameEntity): Long
    suspend fun addManualGame(title: String, description: String, minPlayers: Int, maxPlayers: Int, playtimeMinutes: Int, category: String): Long
    suspend fun updateGame(game: GameEntity)
    suspend fun deleteGame(gameID: Long): Result<Unit>
    fun getAllGames(): Flow<List<GameEntity>>
    fun searchCatalog(query: String): Flow<List<GameEntity>>
    fun getGameCopies(gameID: Long): Flow<GameAndCopies>
    fun getGame(gameID: Long): Flow<GameEntity>
    suspend fun addCopy(gameID: Long): Long
    suspend fun removeCopy(gameID: Long)
}

class GameRepositoryImpl @Inject constructor(
    private val gameDao: GameDao,
    private val gameCopyDao: GameCopyDao,
    private val reservationDao: ReservationDao
): GameRepository {

    override suspend fun addGameFromRemote(details: BggGameDetails): Long {
        // Do not insert and already existing game.
        if (gameDao.getGameByBggId(details.bggId) != null) {
            return 0
        }

        return gameDao.insert(details.createEntity())
    }

    override suspend fun addGame(game: GameEntity): Long {
        return gameDao.insert(game)
    }

    override suspend fun addManualGame(
        title: String,
        description: String,
        minPlayers: Int,
        maxPlayers: Int,
        playtimeMinutes: Int,
        category: String
    ): Long {
        val game = GameEntity(
            title = title,
            description = description,
            minPlayers = minPlayers,
            maxPlayers = maxPlayers,
            playTimeMinutes = playtimeMinutes,
            source = GameSource.MANUAL,
            dateAdded = Clock.System.now().toEpochMilliseconds(),
            imageURL = null,
            thumbnailURL = null,
            category = category,
            bggID = null
        )

        return gameDao.insert(game)
    }

    override suspend fun updateGame(game: GameEntity) {
        gameDao.update(game)
    }

    override suspend fun deleteGame(gameID: Long): Result<Unit> {
        val copiesCount = gameCopyDao.getMaxCopyNumber(gameID)
        val reservationCount = reservationDao.countForGame(gameID)
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

    override fun getGame(gameID: Long): Flow<GameEntity> {
        return gameDao.getGame(gameID).filterNotNull()
    }

    override suspend fun addCopy(gameID: Long): Long {
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

    override suspend fun removeCopy(gameID: Long) {
        gameCopyDao.getAllCopies(gameID).first()
            .maxByOrNull { it.copyNumber }
            ?.let { gameCopyDao.delete(it) }
    }
}