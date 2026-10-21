package com.example.playqueue.modules

import com.example.playqueue.BuildConfig
import com.example.playqueue.data.network.BggAPIClient
import com.example.playqueue.data.network.BggAPIService
import com.example.playqueue.data.network.BggXMLParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * A Module used to hook up the BGG API client and service.
 */
@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    /**
     * Provide the API client singleton.
     * @return The APIClient.
     */
    @Provides
    @Singleton
    fun provideAPIClient(): BggAPIClient {
        return BggAPIClient
    }

    /**
     * Provide the BGG API service singleton.
     * @param apiClient The BGG API client.
     * @return The BggAPIService.
     */
    @Provides
    @Singleton
    fun provideAPIService(apiClient: BggAPIClient): BggAPIService {
        return apiClient.createService(BuildConfig.BGG_API_TOKEN)
    }

    /**
     * Provide the BGG XML Parser.
     * @return The BggXMLParser.
     */
    @Provides
    @Singleton
    fun provideBggXMLParser(): BggXMLParser {
        return BggXMLParser()
    }
}