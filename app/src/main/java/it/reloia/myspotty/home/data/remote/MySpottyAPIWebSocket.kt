package it.reloia.myspotty.home.data.remote

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MySpottyAPIWebSocket(
    private val onFailure: ((Throwable) -> Unit)? = null,
    private val onMessageReceived: (String) -> Unit
) : WebSocketListener() {
    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        onMessageReceived(text)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        println("WebSocket failure: $t")
        onFailure?.let { it(t) }
    }
}