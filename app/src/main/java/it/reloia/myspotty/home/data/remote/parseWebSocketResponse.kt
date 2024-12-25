package it.reloia.myspotty.home.data.remote

import it.reloia.myspotty.home.domain.model.CurrentSong
import it.reloia.myspotty.home.domain.model.WebSocketResponse
import org.json.JSONArray
import org.json.JSONObject

fun parseWebSocketResponse(response: String): WebSocketResponse {
    val json = JSONObject(response)

    return when (val type = json.getString("type")) {
        "init" -> {
            val data = json.getJSONObject("data")

            val currentSong = CurrentSong(
                data.getString("author"),
                data.getString("name"),
                data.getString("song_link"),
                data.getLong("duration"),
                data.getLong("progress"),
                data.getBoolean("explicit"),
                data.getBoolean("playing"),
                data.getString("album_name"),
                data.getString("album_image")
            )

            val recentMessages = json.getJSONArray("recentMessages").map {
                val message = it as JSONObject
                WebSocketResponse.Message(
                    message.getString("username"),
                    message.getString("message")
                )
            }

            WebSocketResponse.Init(
                currentSong,
                recentMessages,
                json.getInt("clients")
            )
        }
        "listening-status" -> {
            val data = json.getJSONObject("data")
            val currentSong = CurrentSong(
                data.getString("author"),
                data.getString("name"),
                data.getString("song_link"),
                data.getLong("duration"),
                data.getLong("progress"),
                data.getBoolean("explicit"),
                data.getBoolean("playing"),
                data.getString("album_name"),
                data.getString("album_image")
            )

            WebSocketResponse.ListeningStatus(
                currentSong,
                json.getInt("clients")
            )
        }
        "chat" -> {
            val data = json.getJSONObject("data")
            WebSocketResponse.Chat(
                WebSocketResponse.Message(
                    data.getString("username"),
                    data.getString("message")
                ),
                json.getInt("clients")
            )
        }
        "paintcanvas" -> {
            val data = json.getJSONObject("data")
            WebSocketResponse.PaintCanvas(
                data.getInt("x"),
                data.getInt("y"),
                data.getString("color")
            )
        }
        else -> throw IllegalArgumentException("Unknown WebSocket response type: $type")
    }
}

fun JSONArray.map(transform: (Any) -> WebSocketResponse.Message): List<WebSocketResponse.Message> {
    val list = mutableListOf<WebSocketResponse.Message>()
    for (i in 0 until length()) {
        list.add(transform(get(i)))
    }
    return list
}