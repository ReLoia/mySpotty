package it.reloia.myspotty.home.domain.model

sealed class WebSocketResponse {
    data class Init(
        val data: CurrentSong,
        val recentMessages: List<Message>,
        val clients: Int,
    ) : WebSocketResponse()

    data class ListeningStatus(
        val data: CurrentSong,
        val clients: Int,
    ) : WebSocketResponse()

    data class Chat(
        val data: Message,
        val clients: Int,
    ) : WebSocketResponse()

    data class PaintCanvas(
        val x: Int,
        val y: Int,
        val color: String,
    ) : WebSocketResponse()

    data class Message(
        val username: String,
        val message: String,
    )
}

/**
 * INIT - OK
 * {
 *    data: {},
 *    clients: wss.clients.size,
 *    recentMessages,
 *    type: "init"
 * }
 *
 * LISTENING STATUS - OK
 * {
 *     data: {},
 *     clients: 0,
 *     type: "listening-status"
 * }
 *
 * CHAT - OK
 * {
 *     type: "chat",
 *     clients: number,
 *     data: {
 *         username: string,
 *         message: string
 *     }
 * }
 *
 * PAINT CANVAS - OK
 * {
 *    type: "paintcanvas",
 *    x: number,
 *    y: number,
 *    color: string,
 * }
 *
 */