package it.reloia.myspotty.home.ui.domain.model

data class CurrentSong(
    val author: String,
    val name: String,
    val songLink: String,
    val duration: Long,
    val progress: Long,
    val explicit: Boolean,
    val playing: Boolean,
    val albumName: String,
    val albumImage: String
)
