package it.reloia.myspotty.home.domain.model

data class CurrentSong(
    val author: String,
    val name: String,
    val song_link: String,
    val duration: Long,
    val progress: Long,
    val explicit: Boolean,
    val playing: Boolean,
    val album_name: String,
    val album_image: String
)
