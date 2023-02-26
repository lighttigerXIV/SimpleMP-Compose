package com.lighttigerxiv.simple.mp.compose.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val image: String? = null
)

@Entity
data class Artist(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val image: String? = null,
    val alreadyRequested: Boolean = false
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Playlist::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("playlist_id"),
            onDelete = ForeignKey.CASCADE
        )]
)
data class PlaylistSong(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val playlist_id: Int,
    val song_id: Long
)