package it.reloia.myspotty.home.data.remote

import android.content.Context
import it.reloia.myspotty.home.data.HomeRepository
import it.reloia.myspotty.home.domain.model.CurrentSong
import it.reloia.myspotty.home.domain.model.LastListened
import it.reloia.myspotty.home.domain.model.SOTD

class RemoteHomeRepository (
    private val apiService: MySpottyApiService,
    private val context: Context
) : HomeRepository {
    override suspend fun getCurrentSong(): CurrentSong? {
        return apiService.getCurrentSong()
    }

    override suspend fun getSOTD(): List<SOTD> {
        return apiService.getSOTD()
    }

    override suspend fun getLastListened(): LastListened {
        TODO("Not yet implemented")
    }
}