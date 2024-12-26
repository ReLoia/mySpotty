package it.reloia.myspotty.home.data.remote

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MySpottyAPIWebSocket(
    private val onFailure: ((Throwable) -> Unit)? = null,
    private val onClosing: ((code: Int, reason: String) -> Unit)? = null,
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

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        onClosing?.let { it(code, reason) }
    }
}