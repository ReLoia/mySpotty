package it.reloia.myspotty.home.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.reloia.myspotty.home.data.HomeRepository
import it.reloia.myspotty.home.domain.model.CurrentSong
import it.reloia.myspotty.home.domain.model.LastListened
import it.reloia.myspotty.home.domain.model.SOTD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel (
    private val repository: HomeRepository
) : ViewModel() {
    private val refreshRequests = Channel<Unit>(1)
    var isRefreshing by mutableStateOf(false)

    private val _currentSong = MutableStateFlow<CurrentSong?>(null)
    val currentSong: StateFlow<CurrentSong?> = _currentSong

    private val _sotd = MutableStateFlow<List<SOTD>>(emptyList())
    val sotd: StateFlow<List<SOTD>> = _sotd

    private val _lastListened = MutableStateFlow<LastListened?>(null)
    val lastListened: StateFlow<LastListened?> = _lastListened

    init {
        getCurrentSong()
        getSOTD()

        viewModelScope.launch {
            for (unit in refreshRequests) {
                isRefreshing = true
                getCurrentSong()
                getSOTD()
                //getLastListened()
                println("Refreshed")
                isRefreshing = false
            }
        }
    }

    fun refresh() {
        refreshRequests.trySend(Unit)
    }

    private fun getCurrentSong() {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                println("Trying to get current song");
                _currentSong.value = repository.getCurrentSong()
            } catch (e: IOException) {
                println("Network error in 'getCurrentSong'. Please check your connection. Error: $e")
            }
        }
    }

    private fun getSOTD() {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                _sotd.value = repository.getSOTD()
            } catch (e: IOException) {
                println("Network error in 'getSOTD'. Please check your connection. Error: $e")
            }
        }
    }

    fun getLastListened() {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                _lastListened.value = repository.getLastListened()
            } catch (e: IOException) {
                println("Network error in 'getLastListened'. Please check your connection. Error: $e")
            }
        }
    }
}