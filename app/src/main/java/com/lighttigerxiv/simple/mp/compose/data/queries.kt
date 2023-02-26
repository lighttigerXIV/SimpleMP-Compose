package com.lighttigerxiv.simple.mp.compose.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PlaylistDao{

    @Query("SELECT * FROM playlist")
    fun getPlaylists(): List<Playlist>

    @Query("DELETE FROM playlist WHERE id = :playlistID")
    fun deletePlaylist( playlistID : Int )

    @Query("UPDATE playlist SET name = :playlistName WHERE id = :playlistID")
    fun updatePlaylistName( playlistName : String, playlistID: Int )

    @Query("UPDATE playlist SET image = :imageString WHERE id = :playlistID")
    fun updatePlaylistImage(imageString: String?, playlistID: Int )


    @Transaction
    @Insert
    fun insertPlaylist(playlist: Playlist)

}

@Dao
interface ArtistsDao{

    @Query("SELECT * FROM artist WHERE id = :id")
    fun getArtist(id: Long): Artist?

    @Query("INSERT INTO artist (id, alreadyRequested) VALUES (:id, 0)")
    fun addArtist(id: Long)

    @Query("UPDATE artist SET image = :image, alreadyRequested = 1 WHERE id = :id")
    fun updateArtistImage(image: String?, id: Long)
}