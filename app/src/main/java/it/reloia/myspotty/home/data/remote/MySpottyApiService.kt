package it.reloia.myspotty.home.data.remote

import it.reloia.myspotty.home.domain.model.CurrentSong
import it.reloia.myspotty.home.domain.model.LastListened
import it.reloia.myspotty.home.domain.model.SOTD
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MySpottyApiService {
    @GET("api")
    suspend fun getCurrentSong(): CurrentSong?

    @GET("api/last")
    suspend fun getLastListened(): LastListened

    @GET("sotd")
    suspend fun getSOTD(): List<SOTD>

    @POST("sotd/url")
    suspend fun addSOTD(@Header("Authorization") password: String, @Body url: String)

    @POST("sotd/remove")
    suspend fun removeSOTD(@Header("Authorization") password: String, @Body url: String)
}