package it.reloia.myspotty.core.domain.model

data class SongInfo(
    val author: String,
    val name: String,
    val explicit: Boolean,
    val album_name: String,
    val album_image: String
)
