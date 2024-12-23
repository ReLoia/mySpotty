package it.reloia.myspotty.home.data

import it.reloia.myspotty.home.domain.model.CurrentSong
import it.reloia.myspotty.home.domain.model.LastListened
import it.reloia.myspotty.home.domain.model.SOTD

interface HomeRepository {
    suspend fun getCurrentSong(): CurrentSong?
    suspend fun getSOTD(): List<SOTD>
    suspend fun getLastListened(): LastListened
    suspend fun addSOTD(url: String, password: String)
}