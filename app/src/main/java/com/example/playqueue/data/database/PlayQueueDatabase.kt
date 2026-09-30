package com.example.playqueue.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * An abstract class for the PlayQueueDatabase that conforms to RoomDatabase.
 */
@Database(entities = [GameEntity::class, GameCopyEntity::class, ReservationEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class PlayQueueDatabase: RoomDatabase() {
    abstract fun gameCopyDao(): GameCopyDao
    abstract fun gameDao(): GameDao
    abstract fun reservationDao(): ReservationDao
}