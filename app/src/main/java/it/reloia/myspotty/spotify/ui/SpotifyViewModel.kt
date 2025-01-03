package it.reloia.myspotty.spotify.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.reloia.myspotty.core.domain.model.SOTD
import it.reloia.myspotty.core.domain.model.SongInfo
import it.reloia.myspotty.spotify.data.SpotifyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class SpotifyViewModel (
    private val spotifyRepository: SpotifyRepository? = null
) : ViewModel() {
    private val _songInfo = MutableStateFlow<SongInfo?>(null)
    val songInfo: StateFlow<SongInfo?> = _songInfo

    private val _sotd = MutableStateFlow<List<SOTD>>(emptyList())
    val sotd: StateFlow<List<SOTD>> = _sotd

    init {
        getSOTD()
    }

    private fun getSOTD() {
        if (spotifyRepository == null) {
            println("Repository is null. Please check your settings.")
            return
        }

        viewModelScope.launch (Dispatchers.IO) {
            try {
                _sotd.value = spotifyRepository.getSOTD()
            } catch (e: IOException) {
                println("Network error in 'getSOTD'. Please check your connection. Error: $e")
            }
        }
    }

    fun getSongInfo(songId: String, password: String) {
        if (spotifyRepository == null) {
            println("Repository is null. Please check your settings.")
            return
        }

        viewModelScope.launch {
            val songInfo = spotifyRepository.getSongInfo(songId, password)
            if (songInfo != null) _songInfo.value = songInfo
        }
    }

    fun addToSOTD(url: String, password: String) {
        if (spotifyRepository == null) {
            println("Repository is null. Please check your settings.")
            return
        }

        viewModelScope.launch {
            spotifyRepository.addToSOTD(url, password)
        }
    }

    fun removeFromSOTD(url: String, password: String) {
        if (spotifyRepository == null) {
            println("Repository is null. Please check your settings.")
            return
        }

        viewModelScope.launch {
            spotifyRepository.removeFromSOTD(url, password)
        }
    }
}