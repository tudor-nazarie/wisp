package wisp.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import wisp.network.models.Match
import wisp.network.models.Player
import wisp.network.models.PlayerMatch

interface OpenDotaService {
    @GET("players/{id}")
    suspend fun getPlayer(@Path("id") id: Long): Response<Player>

    @GET("players/{id}/matches")
    suspend fun getPlayerMatches(@Path("id") id: Long): Response<List<PlayerMatch>>

    @GET("players/{id}/matches")
    suspend fun getPlayerMatches(@Path("id") id: Long, @Query("limit") limit: Int): Response<List<PlayerMatch>>

    @GET("matches/{id}")
    suspend fun getMatch(@Path("id") id: Long): Response<Match>
}
