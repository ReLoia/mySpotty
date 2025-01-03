package it.reloia.myspotty.spotify.data.remote

import it.reloia.myspotty.core.data.api.MySpottyApiService
import it.reloia.myspotty.core.domain.model.SOTD
import it.reloia.myspotty.core.domain.model.SongInfo
import it.reloia.myspotty.core.domain.model.request.SOTDUrlRequest
import it.reloia.myspotty.spotify.data.SpotifyRepository

class RemoteSpotifyRepository (
    private val apiService: MySpottyApiService,
) : SpotifyRepository {
    override suspend fun getSongInfo(songId: String, password: String): SongInfo? {
        val response = apiService.getSongInfo(password, songId)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    override suspend fun getSOTD(): List<SOTD> {
        val response = apiService.getSOTD()
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }

    override suspend fun addToSOTD(url: String, password: String) {
        apiService.addSOTD(password, SOTDUrlRequest(url))
    }

    override suspend fun removeFromSOTD(url: String, password: String) {
        apiService.removeSOTD(password, SOTDUrlRequest(url))
    }

}