package com.lighttigerxiv.simple.mp.compose.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PlaylistDao{

    @Query("SELECT * FROM playlist")
    fun getAllPlaylists(): List<Playlist>

    @Query("DELETE FROM playlist WHERE id = :playlistID")
    fun deletePlaylist( playlistID : Int )

    @Query("UPDATE playlist SET name = :playlistName WHERE id = :playlistID")
    fun updatePlaylistName( playlistName : String, playlistID: Int )


    @Query("UPDATE playlist SET songs = :songsJson WHERE id = :playlistID")
    fun updatePlaylistSongs( songsJson : String, playlistID: Int )


    @Transaction
    @Insert
    fun insertPlaylist(playlist: Playlist)

}