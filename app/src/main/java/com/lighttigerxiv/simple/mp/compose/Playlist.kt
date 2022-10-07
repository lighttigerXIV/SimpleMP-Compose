package com.lighttigerxiv.simple.mp.compose

data class Playlist(
    val id : Int,
    val name : String,
    val image : String?,
    val songs : ArrayList<Song>
)
