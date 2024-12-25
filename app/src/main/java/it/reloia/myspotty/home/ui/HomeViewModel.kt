package it.reloia.myspotty.home.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.reloia.myspotty.home.data.HomeRepository
import it.reloia.myspotty.home.data.remote.MySpottyAPIWebSocket
import it.reloia.myspotty.home.data.remote.parseWebSocketResponse
import it.reloia.myspotty.home.domain.model.CurrentSong
import it.reloia.myspotty.home.domain.model.LastListened
import it.reloia.myspotty.home.domain.model.SOTD
import it.reloia.myspotty.home.domain.model.WebSocketResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.io.EOFException
import java.io.IOException

class HomeViewModel (
    private val repository: HomeRepository
) : ViewModel() {
    private val refreshRequests = Channel<Unit>(1)
    var isRefreshing = mutableStateOf(false)

    private val _currentSong = MutableStateFlow<CurrentSong?>(null)
    val currentSong: StateFlow<CurrentSong?> = _currentSong

    private val _sotd = MutableStateFlow<List<SOTD>>(emptyList())
    val sotd: StateFlow<List<SOTD>> = _sotd

    private val _lastListened = MutableStateFlow<LastListened?>(null)
    val lastListened: StateFlow<LastListened?> = _lastListened

    private val _isSOTDSheetOpen = mutableStateOf(false)
    val isSOTDSheetOpen: State<Boolean> = _isSOTDSheetOpen

    private val _currentSelectedSOTD = mutableStateOf<SOTD?>(null)
    val currentSelectedSOTD: State<SOTD?> = _currentSelectedSOTD

    private val webSocketClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    init {
        getCurrentSong()
        getSOTD()
        getLastListened()

        viewModelScope.launch {
            for (unit in refreshRequests) {
                isRefreshing.value = true
                getCurrentSong()
                getSOTD()
                getLastListened()
                delay(200L)
                isRefreshing.value = false
            }
        }

        connectToWebSocket()
    }

    fun refresh() {
        refreshRequests.trySend(Unit)
    }

    fun toggleSOTDSheet(sotd: SOTD?) {
        if (sotd == null) {
            _currentSelectedSOTD.value = null
            _isSOTDSheetOpen.value = false
        } else {
            _currentSelectedSOTD.value = sotd
            _isSOTDSheetOpen.value = true
        }
    }

    // remote methods

    // SOCKETS

    fun startWebSocket() {
        if (webSocket == null) {
            connectToWebSocket()
        }
    }

    fun stopWebSocket() {
        webSocket?.close(1000, "User requested")
        webSocket = null
    }

    private fun connectToWebSocket() {
        val request = Request.Builder()
            .url("wss://reloia.ddns.net/myspottyapi")
            .build()

        val listener = MySpottyAPIWebSocket { message ->
            println("Received message: $message")
            val parsed: WebSocketResponse = parseWebSocketResponse(message)
            when (parsed) {
//                is WebSocketResponse.Chat -> {
//
//                }
//                is WebSocketResponse.Init -> {
//
//                }
                is WebSocketResponse.ListeningStatus -> {
                    _currentSong.value = parsed.data
                }
                else -> {
                    // TODO: handle other types
                }
//                is WebSocketResponse.PaintCanvas -> TODO()
            }
        }

        webSocket = webSocketClient.newWebSocket(request, listener)
    }

    // API

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

    fun removeFromSOTD(date: Long, password: String) {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                repository.removeSOTD(date, password)
            } catch (e: IOException) {
                println("Network error in 'removeFromSOTD'. Please check your connection. Error: $e")
            }
        }
    }
}