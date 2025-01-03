package it.reloia.myspotty.spotify.data

import it.reloia.myspotty.core.domain.model.SOTD
import it.reloia.myspotty.core.domain.model.SongInfo

interface SpotifyRepository {
    suspend fun getSongInfo(songId: String, password: String): SongInfo?
    suspend fun getSOTD(): List<SOTD>
    suspend fun addToSOTD(url: String, password: String)
    suspend fun removeFromSOTD(url: String, password: String)
}