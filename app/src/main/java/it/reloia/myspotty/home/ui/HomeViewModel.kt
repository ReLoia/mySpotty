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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.EOFException
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

    // TODO: add support for websockets using okhttp

    init {
        getCurrentSong()
        getSOTD()
        getLastListened()

        viewModelScope.launch {
            for (unit in refreshRequests) {
                isRefreshing = true
                getCurrentSong()
                getSOTD()
                getLastListened()
                delay(200L)
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
                _currentSong.value = repository.getCurrentSong()
            } catch (e: IOException) {
                println("Network error in 'getCurrentSong'. Please check your connection. Error: $e")
                _currentSong.value = null
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

    private fun getLastListened() {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                _lastListened.value = repository.getLastListened()
            } catch (e: EOFException) {
                _lastListened.value = null
                println("EOF error in 'getLastListened'. Please check your connection. Error: $e")
            } catch (e: IOException) {
                println("Network error in 'getLastListened'. Please check your connection. Error: $e")
            }
        }
    }

    fun addToSOTD(url: String, password: String) {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                repository.addSOTD(url, password)
            } catch (e: IOException) {
                println("Network error in 'addToSOTD'. Please check your connection. Error: $e")
            }
        }
    }

    fun removeFromSOTD(url: String, password: String) {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                repository.removeSOTD(url, password)
            } catch (e: IOException) {
                println("Network error in 'removeFromSOTD'. Please check your connection. Error: $e")
            }
        }
    }
}