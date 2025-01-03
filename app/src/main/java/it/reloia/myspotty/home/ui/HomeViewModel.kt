package it.reloia.myspotty.home.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.reloia.myspotty.home.data.HomeRepository
import it.reloia.myspotty.core.data.api.MySpottyAPIWebSocket
import it.reloia.myspotty.core.utility.parseWebSocketResponse
import it.reloia.myspotty.core.domain.model.CurrentSong
import it.reloia.myspotty.core.domain.model.LastListened
import it.reloia.myspotty.core.domain.model.SOTD
import it.reloia.myspotty.core.domain.model.WebSocketResponse
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
    private val repository: HomeRepository? = null
) : ViewModel() {
    // LOCAL - Refresh

    private val refreshRequests = Channel<Unit>(1)
    var isRefreshing = mutableStateOf(false)

    // REMOTE - API

    private val _currentSong = MutableStateFlow<CurrentSong?>(null)
    val currentSong: StateFlow<CurrentSong?> = _currentSong

    private val _sotd = MutableStateFlow<List<SOTD>>(emptyList())
    val sotd: StateFlow<List<SOTD>> = _sotd

    private val _lastListened = MutableStateFlow<LastListened?>(null)
    val lastListened: StateFlow<LastListened?> = _lastListened

    // LOCAL - SOTD Sheet

    private val _isSOTDSheetOpen = mutableStateOf(false)
    val isSOTDSheetOpen: State<Boolean> = _isSOTDSheetOpen

    private val _currentSelectedSOTD = mutableStateOf<SOTD?>(null)
    val currentSelectedSOTD: State<SOTD?> = _currentSelectedSOTD

    // REMOTE - WebSockets

    private val _isWebSocketConnected = mutableStateOf(false)
    val isWebSocketConnected: State<Boolean> = _isWebSocketConnected

    private val webSocketClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    // LOCAL - Progress bar

    private val _progress = mutableLongStateOf(0L)
    val progress: State<Long> = _progress

    init {
        getCurrentSong()
        getSOTD()
        getLastListened()

        viewModelScope.launch {
            for (unit in refreshRequests) {
                isRefreshing.value = true
                getCurrentSong()
                getSOTD()
                if (webSocket == null)
                    connectToWebSocket()
                getLastListened()

                delay(200L)
                isRefreshing.value = false
            }
        }

        connectToWebSocket()
        viewModelScope.launch (Dispatchers.IO) {
            while (true) {
                if (_currentSong.value?.playing == true) {
                    delay(1000L)
                    _progress.longValue += 1000L
                }
            }
        }
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
        _isWebSocketConnected.value = false
    }

    private fun connectToWebSocket() {
        if (repository == null) {
            println("WebSocket: base URL is empty. Please set it in the settings.")
            return
        }

        val baseURL = repository.baseURL.replace(regex = Regex("http(s)?://"), replacement = "wss://")

        if (baseURL.isEmpty()) {
            println("WebSocket: base URL is empty. Please set it in the settings.")
            return
        }

        val request = Request.Builder()
            .url(baseURL)
            .build()

        val listener = MySpottyAPIWebSocket (
            onFailure = { throwable ->
                println("WebSocket failure: $throwable")
                _isWebSocketConnected.value = false
                webSocket = null
            },
            onClosing = { code, reason ->
                println("WebSocket closing: $code, $reason")
                _isWebSocketConnected.value = false
                webSocket = null
            },
        ) { message ->
            when (val parsed: WebSocketResponse = parseWebSocketResponse(message)) {
                is WebSocketResponse.Init -> {
                    if (parsed.data.name != _currentSong.value?.name) {
                        viewModelScope.launch {
                            getLastListened()
                        }
                    }
                    _currentSong.value = parsed.data
                    _progress.longValue = parsed.data.progress
                }
                is WebSocketResponse.ListeningStatus -> {
                    if (parsed.data.name != _currentSong.value?.name) {
                        viewModelScope.launch {
                            getLastListened()
                        }
                    }
                    _currentSong.value = parsed.data
                    _progress.longValue = parsed.data.progress
                }
                else -> { }
            }
        }

        webSocket = webSocketClient.newWebSocket(request, listener)
        _isWebSocketConnected.value = true
    }

    // API

    private fun getCurrentSong() {
        if (repository == null) {
            println("Repository is null. Please check your settings.")
            return
        }

        viewModelScope.launch (Dispatchers.IO) {
            try {
                _currentSong.value = repository.getCurrentSong()

                _progress.longValue = _currentSong.value?.progress ?: 0L
            } catch (e: IOException) {
                println("Network error in 'getCurrentSong'. Please check your connection. Error: $e")
                _currentSong.value = null
            }
        }
    }

    private fun getSOTD() {
        if (repository == null) {
            println("Repository is null. Please check your settings.")
            return
        }

        viewModelScope.launch (Dispatchers.IO) {
            try {
                _sotd.value = repository.getSOTD()
            } catch (e: IOException) {
                println("Network error in 'getSOTD'. Please check your connection. Error: $e")
            }
        }
    }

    private fun getLastListened() {
        if (repository == null) {
            println("Repository is null. Please check your settings.")
            return
        }

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
        if (repository == null) {
            println("Repository is null. Please check your settings.")
            return
        }

        viewModelScope.launch (Dispatchers.IO) {
            try {
                if (repository.addSOTD(url, password)) {
                    getSOTD()
                }
            } catch (e: IOException) {
                println("Network error in 'addToSOTD'. Please check your connection. Error: $e")
            }
        }
    }

    fun removeFromSOTD(url: String, password: String) {
        if (repository == null) {
            println("Repository is null. Please check your settings.")
            return
        }

        viewModelScope.launch (Dispatchers.IO) {
            try {
                if (repository.removeSOTD(url, password)) {
                    getSOTD()
                }
            } catch (e: IOException) {
                println("Network error in 'removeFromSOTD'. Please check your connection. Error: $e")
            }
        }
    }

    fun removeFromSOTD(date: Long, password: String) {
        if (repository == null) {
            println("Repository is null. Please check your settings.")
            return
        }

        viewModelScope.launch (Dispatchers.IO) {
            try {
                if (repository.removeSOTD(date, password)) {
                    getSOTD()
                }
            } catch (e: IOException) {
                println("Network error in 'removeFromSOTD'. Please check your connection. Error: $e")
            }
        }
    }
}