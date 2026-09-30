package com.example.playqueue.modules

import android.content.Context
import androidx.room.Room
import com.example.playqueue.data.database.Converter
import com.example.playqueue.data.database.GameCopyDao
import com.example.playqueue.data.database.GameDao
import com.example.playqueue.data.database.PlayQueueDatabase
import com.example.playqueue.data.database.ReservationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * A Module used to hook up the database and DAOs.
 */
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    /**
     * Provide the database singleton after building it.
     * @param context The context of the application.
     * @return The PlayQueueDatabase.
     */
    @Provides
    @Singleton
    fun provideRecipeDatabase(@ApplicationContext context: Context): PlayQueueDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = PlayQueueDatabase::class.java,
            name = "recipe_db"
        )
            .addTypeConverter(Converter())
            .build()
    }

    /**
     * Provide the GameDao singleton from the database.
     * @param database The Room database.
     * @return The GameDao.
     */
    @Provides
    @Singleton
    fun provideGameDao(database: PlayQueueDatabase): GameDao {
        return database.gameDao()
    }

    /**
     * Provide the GameCopyDao singleton from the database.
     * @param database The Room database.
     * @return The GameCopyDao.
     */
    @Provides
    @Singleton
    fun provideGameCopyDao(database: PlayQueueDatabase): GameCopyDao {
        return database.gameCopyDao()
    }

    /**
     * Provide the ReservationDao singleton from the database.
     * @param database The Room database.
     * @return The ReservationDao.
     */
    @Provides
    @Singleton
    fun provideReservationDao(database: PlayQueueDatabase): ReservationDao {
        return database.reservationDao()
    }
}