package it.reloia.myspotty.core.data.api

import it.reloia.myspotty.core.domain.model.CurrentSong
import it.reloia.myspotty.core.domain.model.LastListened
import it.reloia.myspotty.core.domain.model.SOTD
import it.reloia.myspotty.core.domain.model.SongInfo
import it.reloia.myspotty.core.domain.model.request.SOTDDateRequest
import it.reloia.myspotty.core.domain.model.request.SOTDUrlRequest
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface MySpottyApiService {
    @GET("api")
    suspend fun getCurrentSong(): Response<CurrentSong?>

    @GET("api/last")
    suspend fun getLastListened(): Response<LastListened?>

    @GET("sotd")
    suspend fun getSOTD(): Response<List<SOTD>>

    @POST("sotd/url")
    suspend fun addSOTD(@Header("Authorization") password: String, @Body data: SOTDUrlRequest): Response<JSONObject>

    @POST("sotd/remove/url")
    suspend fun removeSOTD(@Header("Authorization") password: String, @Body data: SOTDUrlRequest): Response<Unit>

    @POST("sotd/remove/date")
    suspend fun removeSOTD(@Header("Authorization") password: String, @Body data: SOTDDateRequest): Response<Unit>

    @GET("song-info")
    suspend fun getSongInfo(@Header("Authorization") password: String, @Query("id") id: String): Response<SongInfo>
}