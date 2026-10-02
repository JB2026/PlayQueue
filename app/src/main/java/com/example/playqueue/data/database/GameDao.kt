package com.example.playqueue.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * The Dao interface for the Games database defining all the possible methods for interacting with the database.
 */
@Dao
interface GameDao {
    /**
     * A suspend method to add a base game.
     * @param game The GameEntity to add to the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: GameEntity): Long

    /**
     * A suspend method to update an existing base game in the database.
     * @param game The GameEntity to update in the database.
     */
    @Update
    suspend fun update(game: GameEntity)

    /**
     * A suspend method to delete an existing base game in the database.
     * @param game The GameEntity to delete in the database.
     */
    @Delete
    suspend fun delete(game: GameEntity)

    /**
     * A suspend method to get a specific base game.
     * @param id The ID of the base game to get from the database.
     */
    @Query("SELECT * FROM games WHERE id = :id")
    fun getGame(id: Long): Flow<GameEntity?>

    /**
     * A method to get all base games from the database in ascending order based on their name.
     */
    @Query("SELECT * FROM games ORDER BY title ASC")
    fun getAllGames(): Flow<List<GameEntity>>

    /**
     * A method to search for base games that match the search term.
     * @param searchTerm The search term to make the search with.
     */
    @Query("SELECT * FROM games WHERE title LIKE '%' || :searchTerm || '%'")
    fun searchGamesByTitle(searchTerm: String): Flow<List<GameEntity>>

    /**
     * A suspend method to get a specific base game by BGG ID.
     * @param bggId The BGG ID of the base game to get from the database.
     */
    @Query("SELECT * FROM games WHERE bggID = :bggID LIMIT 1")
     fun getGameByBggId(bggID: Int): GameEntity?
}