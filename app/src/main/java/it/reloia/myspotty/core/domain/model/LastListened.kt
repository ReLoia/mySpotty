package it.reloia.myspotty.core.domain.model

data class LastListened(
    val author: String,
    val name: String,
    val song_link: String,
    val explicit: Boolean,
    val album_name: String,
    val album_image: String
)
