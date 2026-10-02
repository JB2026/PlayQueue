package com.example.playqueue.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * The Dao interface for the Game Copy database defining all the possible methods for interacting with the database.
 */
@Dao
interface GameCopyDao {
    /**
     * A suspend method to add a game copy to the database.
     * @param copy The GameCopyEntity to add to the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(copy: GameCopyEntity): Long

    /**
     * A suspend method to update an existing game copy in the database.
     * @param copy The GameCopyEntity to update in the database.
     */
    @Update
    suspend fun update(copy: GameCopyEntity)

    /**
     * A suspend method to delete an existing game copy in the database.
     * @param copy The GameCopyEntity to delete in the database.
     */
    @Delete
    suspend fun delete(copy: GameCopyEntity)

    /**
     * A method to get all matching game copies for a specific ID.
     * @param gameId The game ID to make the query with.
     */
    @Query("SELECT * FROM game_copies WHERE gameID = :gameID ORDER BY copyNumber ASC")
    fun getAllCopies(gameID: Long): Flow<List<GameCopyEntity>>

    /**
     * A suspend method to get a specific game copy.
     * @param id The ID of the game copy to get from the database.
     */
    @Query("SELECT * FROM game_copies WHERE id = :id")
    fun getCopy(id: Long): Flow<GameCopyEntity?>

    /**
     * A suspend method to get the numbers of copies of a game.
     * @param gameId The ID of the game to get from the database.
     */
    @Query("SELECT COALESCE(MAX(copyNumber), 0) FROM game_copies WHERE gameId = :gameID")
    suspend fun getMaxCopyNumber(gameID: Long): Int
}