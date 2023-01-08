package com.lighttigerxiv.simple.mp.compose.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val image : String? = null,
    val songs: String? = null
)

@Entity
data class Artist(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val image: String? = null,
    val alreadyRequested: Boolean = false
)