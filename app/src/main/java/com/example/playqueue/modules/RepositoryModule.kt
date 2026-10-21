package com.example.playqueue.modules

import com.example.playqueue.data.database.GameCopyDao
import com.example.playqueue.data.database.GameDao
import com.example.playqueue.data.database.ReservationDao
import com.example.playqueue.data.network.BggAPIService
import com.example.playqueue.data.network.BggXMLParser
import com.example.playqueue.data.repositories.BggRepository
import com.example.playqueue.data.repositories.BggRepositoryImpl
import com.example.playqueue.data.repositories.GameRepository
import com.example.playqueue.data.repositories.GameRepositoryImpl
import com.example.playqueue.data.repositories.SearchRepository
import com.example.playqueue.data.repositories.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    /**
     * Provide the game repository singleton after building it.
     * @param gameDao The game DAO.
     * @param gameCopyDao The game copy DAO.
     * @param reservationDao The reservation DAO.
     * @return The RecipeRepository.
     */
    @Provides
    @Singleton
    fun provideGameRepository(
        gameDao: GameDao,
        gameCopyDao: GameCopyDao,
        reservationDao: ReservationDao
    ): GameRepository {
        return GameRepositoryImpl(
            gameDao,
            gameCopyDao,
            reservationDao
        )
    }

    /**
     * Provide the search repository singleton after building it.
     * @param gameDao The game DAO.
     * @param api The BGG API SERVICE.
     * @param xmlParser The BGG XML Parser.
     * @return The RecipeRepository.
     */
    @Provides
    @Singleton
    fun provideSearchRepository(
        gameDao: GameDao,
        bggRepository: BggRepository
    ): SearchRepository {
        return SearchRepositoryImpl(gameDao, bggRepository)
    }

    /**
     * Provide the BGG repository singleton after building it.
     * @param bggAPIService The BGG API SERVICE.
     * @param xmlParser The BGG XML Parser.
     * @return The RecipeRepository.
     */
    @Provides
    @Singleton
    fun provideBggRepository(
        bggAPIService: BggAPIService,
        xmlParser: BggXMLParser
    ): BggRepository {
        return BggRepositoryImpl(bggAPIService = bggAPIService, xmlParser = xmlParser)
    }
}