package wisp.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import wisp.network.models.Match
import wisp.network.models.Player
import wisp.network.models.PlayerMatch
import wisp.utils.R

private val contentType: MediaType = "application/json".toMediaType()
val openDotaService: OpenDotaService =
    Retrofit.Builder()
        .baseUrl("https://api.opendota.com/api/")
        .addConverterFactory(R.json.asConverterFactory(contentType))
        .build()
        .create(OpenDotaService::class.java)

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
