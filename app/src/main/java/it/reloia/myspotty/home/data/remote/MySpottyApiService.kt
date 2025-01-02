package it.reloia.myspotty.home.data.remote

import it.reloia.myspotty.home.domain.model.CurrentSong
import it.reloia.myspotty.home.domain.model.LastListened
import it.reloia.myspotty.home.domain.model.SOTD
import it.reloia.myspotty.home.domain.model.SOTDDateRequest
import it.reloia.myspotty.home.domain.model.SOTDUrlRequest
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

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
}