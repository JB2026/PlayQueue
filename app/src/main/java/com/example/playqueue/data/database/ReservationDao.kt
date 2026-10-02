package com.example.playqueue.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playqueue.data.ReservationStatus
import kotlinx.coroutines.flow.Flow


/**
 * The Dao interface for the Reservation database defining all the possible methods for interacting with the database.
 */
@Dao
interface ReservationDao {
    /**
     * A suspend method to add a reservation to the database.
     * @param reservation The ReservationEntity to add to the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reservation: ReservationEntity): Long

    /**
     * A suspend method to update an existing reservation in the database.
     * @param reservation The GameEntity to update in the database.
     */
    @Update
    suspend fun update(reservation: ReservationEntity)

    /**
     * A suspend method to delete an existing reservation in the database.
     * @param reservation The ReservationEntity to delete in the database.
     */
    @Delete
    suspend fun delete(reservation: ReservationEntity)

    /**
     * A suspend method to get a specific reservation.
     * @param id The ID of the reservation to get from the database.
     */
    @Query("SELECT * FROM reservations WHERE id = :id")
    fun getReservation(id: Long): Flow<ReservationEntity?>

    /**
     * A method to get all matching reservations for a specific game copy ID.
     * @param copyId The game copy ID to make the query with.
     */
    @Query("SELECT * FROM reservations WHERE gameCopyID = :copyID || '%'")
    fun getReservationsForCopy(copyID: Long): Flow<List<ReservationEntity>>

    /**
     * A method to get all matching reservations for a specific game copy ID in a specific range.
     * @param gameId The game ID to make the query with.
     * @param rangeStart The date range start.
     * @param rangeEnd The date range end.
     */
    @Query(
        """
        SELECT res.* FROM reservations AS res
        INNER JOIN game_copies AS gc ON res.gameCopyId = gc.id
        WHERE gc.gameId = :gameID
          AND res.startDate < :rangeEnd
          AND res.endDate > :rangeStart
          AND res.status != 'CANCELLED'
        ORDER BY res.startDate ASC
        """
    )
    fun getMatchingReservationsInRange(gameID: Long, rangeStart: Long, rangeEnd: Long): Flow<List<ReservationEntity>>

    /**
     * A method to get all matching reservations for a specific game copy ID that are not complete.
     * @param gameId The game ID to make the query with.
     */
    @Query(
        """
        SELECT res.* FROM reservations AS res
        INNER JOIN game_copies AS gc ON res.gameCopyId = gc.id
        WHERE gc.gameId = :gameID
          AND res.status != 'CANCELLED'
          AND res.status != 'COMPLETE'
        ORDER BY res.startDate ASC
        """
    )
    fun getMatchingReservations(gameID: Long): Flow<List<ReservationEntity>>

    /**
     * A method to get all reservations for a specific time range.
     * @param rangeStart The date range start.
     * @param rangeEnd The date range end.
     */
    @Query(
        """
        SELECT * FROM reservations
        WHERE startDate < :rangeEnd
          AND endDate > :rangeStart
          AND status != 'CANCELLED'
        ORDER BY startDate ASC
        """
    )
    fun getReservationsInRange(rangeStart: Long, rangeEnd: Long): Flow<List<ReservationEntity>>

    /**
     * A method to get all reservations with a matching status.
     * @param status The reservation status to match.
     */
    @Query("SELECT * FROM reservations WHERE status = :status ORDER BY endDate ASC")
    fun getReservationsByStatus(status: ReservationStatus): Flow<List<ReservationEntity>>
}