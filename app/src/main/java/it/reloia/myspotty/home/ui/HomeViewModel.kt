package it.reloia.myspotty.home.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
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
import kotlinx.coroutines.Job
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

    private var progressJob: Job? = null

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

    private fun startProgressJob(currentSong: CurrentSong) {
        if (currentSong.playing) {
            progressJob?.cancel()
            progressJob = null

            _progress.longValue = currentSong.progress
            progressJob = viewModelScope.launch {
                while (true) {
                    delay(1000L)
                    _progress.longValue += 1000L
                }
            }
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
        val request = Request.Builder()
            .url("wss://reloia.ddns.net/myspottyapi")
            .build()

        val listener = MySpottyAPIWebSocket (
            onFailure = { throwable ->
                println("WebSocket failure: $throwable")
                _isWebSocketConnected.value = false
            }
        ) { message ->
            println("Received message: $message")
            when (val parsed: WebSocketResponse = parseWebSocketResponse(message)) {
//                is WebSocketResponse.Chat -> {
//
//                }
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
                else -> {
                    // TODO: handle other types
                }
//                is WebSocketResponse.PaintCanvas -> TODO()
            }
        }

        webSocket = webSocketClient.newWebSocket(request, listener)
        _isWebSocketConnected.value = true
    }

    // API

    private fun getCurrentSong() {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                _currentSong.value = repository.getCurrentSong()

                startProgressJob(_currentSong.value!!)
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