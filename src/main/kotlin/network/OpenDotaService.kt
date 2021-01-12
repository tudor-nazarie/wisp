package network

import network.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenDotaService {
    @GET("players/{id}")
    suspend fun getPlayer(@Path("id") id: Long): Response<Player>

    @GET("heroes")
    suspend fun getHeroes(): Response<List<Hero>>

    @GET("players/{id}/heroes")
    suspend fun getPlayerHeroes(@Path("id") id: Long): Response<List<PlayerHero>>

    @GET("players/{id}/matches")
    suspend fun getPlayerMatches(@Path("id") id: Long): Response<List<PlayerMatch>>

    @GET("players/{id}/matches")
    suspend fun getPlayerMatches(@Path("id") id: Long, @Query("limit") limit: Int): Response<List<PlayerMatch>>

    @GET("players/{id}/wl")
    suspend fun getPlayerWinLoss(@Path("id") id: Long): Response<PlayerWinLoss>

    @GET("players/{id}/wl")
    suspend fun getPlayerWinLoss(@Path("id") id: Long, @Query("limit") limit: Int): Response<PlayerWinLoss>
}
