package it.reloia.myspotty.home.domain.model

data class LastListened(
    val author: String,
    val name: String,
    val songLink: String,
    val explicit: Boolean,
    val albumName: String,
    val albumImage: String
)
