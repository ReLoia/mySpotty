package it.reloia.myspotty.core.domain.model

data class SOTD(
    val name: String,
    val author: String,
    val date: Long,
    val album: String,
    val url: String
)
