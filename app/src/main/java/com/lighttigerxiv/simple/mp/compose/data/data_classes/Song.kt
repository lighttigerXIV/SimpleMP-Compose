package com.lighttigerxiv.simple.mp.compose.data.data_classes

data class Song(

    val id: Long,
    var path: String,
    val title: String,
    val albumID: Long,
    val duration: Int,
    val artistID: Long,
    val year: Int,
    val genre: String,
    val modificationDate: Long,
    var selected: Boolean = false
)