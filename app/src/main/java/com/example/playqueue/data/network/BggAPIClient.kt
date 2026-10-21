package com.example.playqueue.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * An object to handle providing Retrofit and OkHttp clients.
 */
object BggAPIClient {
    /// The base URL for the BoardGameGeek API
    private const val BASE_URL = "https://boardgamegeek.com/xmlapi2/"

    fun createService(
        apiToken: String?
    ): BggAPIService {
        /// The OKHttp client
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(BggAuthInterceptor(apiToken))
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(BggAPIService::class.java)
    }
}