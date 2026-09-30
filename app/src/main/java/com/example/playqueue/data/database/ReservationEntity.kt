package com.example.playqueue.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playqueue.data.ReservationStatus


/**
 * A data class that represents a reservation entity used in the Room database.
 */
@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey val id: Long,
    val gameCopyID: Long,
    val customerName: String,
    val startDate: Long,
    val endDate: Long,
    val checkedOutData: Long?,
    val returnedDate: Long?,
    val status: ReservationStatus,
    val notes: String?
)
