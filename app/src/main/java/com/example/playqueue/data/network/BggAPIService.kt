package com.example.playqueue.data.network

import okhttp3.Interceptor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * An interface that provides all the main BoardGameGeek API calls.
 */
interface BggAPIService {
    /**
     * A suspend method to search for a game.
     * @param query The search term.
     * @param type The type of board game. The default is board game and board game expansion.
     * @return A XML string response.
     */
    @GET("search")
    suspend fun search(
        @Query("query") query: String,
        @Query("type") type: String = "boardgame,boardgameexpansion"
    ): Response<String>

    /**
     * A suspend method to get a specific game for a game.
     * @param bggID The ID for the game
     * @return A XML string response.
     */
    @GET("thing")
    suspend fun getGame(
        @Query("id") bggID: Int
    ): Response<String>
}

class BggAuthInterceptor(private val token: String?): Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()
        val request = if (token.isNullOrBlank()) {
            originalRequest
        } else {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }

        return chain.proceed(request)
    }
}