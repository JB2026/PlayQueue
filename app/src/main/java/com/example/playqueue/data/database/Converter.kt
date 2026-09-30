package com.example.playqueue.data.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.playqueue.data.GameSource
import com.example.playqueue.data.GameStatus
import com.example.playqueue.data.ReservationStatus

/**
 * A type converter class to handle the storing and un-storing of different statuses from a Room database.
 */
@ProvidedTypeConverter
class Converter {
    @TypeConverter
    fun fromGameSource(value: GameSource): String = value.name

    @TypeConverter
    fun toGameSource(value: String): GameSource = GameSource.valueOf(value)

    @TypeConverter
    fun fromGameStatus(value: GameStatus): String = value.name

    @TypeConverter
    fun toGameStatus(value: String): GameStatus = GameStatus.valueOf(value)

    @TypeConverter
    fun fromReservationStatus(value: ReservationStatus): String = value.name

    @TypeConverter
    fun toReservationStatus(value: String): ReservationStatus = ReservationStatus.valueOf(value)
}