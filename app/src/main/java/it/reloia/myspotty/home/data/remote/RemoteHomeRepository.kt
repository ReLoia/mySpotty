package it.reloia.myspotty.home.data.remote

import it.reloia.myspotty.core.data.api.MySpottyApiService
import it.reloia.myspotty.home.data.HomeRepository
import it.reloia.myspotty.core.domain.model.CurrentSong
import it.reloia.myspotty.core.domain.model.LastListened
import it.reloia.myspotty.core.domain.model.SOTD
import it.reloia.myspotty.core.domain.model.request.SOTDDateRequest
import it.reloia.myspotty.core.domain.model.request.SOTDUrlRequest

class RemoteHomeRepository (
    private val apiService: MySpottyApiService,
    override val baseURL: String,
) : HomeRepository {
    override suspend fun getCurrentSong(): CurrentSong? {
        val response = apiService.getCurrentSong()
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

    override suspend fun getLastListened(): LastListened? {
        val response = apiService.getLastListened()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    override suspend fun addSOTD(url: String, password: String): Boolean {
        val response = apiService.addSOTD(password, SOTDUrlRequest(url))

        return response.isSuccessful
    }

    override suspend fun removeSOTD(url: String, password: String): Boolean {
        val response = apiService.removeSOTD(password, SOTDUrlRequest(url))
        return response.isSuccessful
    }

    override suspend fun removeSOTD(date: Long, password: String): Boolean {
        val response = apiService.removeSOTD(password, SOTDDateRequest(date))
        return response.isSuccessful
    }
}