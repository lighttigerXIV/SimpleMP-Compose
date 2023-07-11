package com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.add_songs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.mongodb.getMongoRealm
import com.lighttigerxiv.simple.mp.compose.data.mongodb.queries.PlaylistsQueries
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.PlaylistScreenVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddSongsScreenVM(application: Application) : AndroidViewModel(application){

    //************************************************
    // Data
    //************************************************

    data class SongInAddSongs(
        val id: Long,
        val title: String,
        val selected: Boolean,
        val albumID: Long,
        val artistID: Long
    )


    //************************************************
    // Variables
    //************************************************

    private val playlistsQueries = PlaylistsQueries(getMongoRealm())

    private val _screenLoaded = MutableStateFlow(false)
    val screenLoaded = _screenLoaded.asStateFlow()

    private val _songs = MutableStateFlow<List<SongInAddSongs>?>(null)
    val songs = _songs.asStateFlow()



    //************************************************
    // Functions
    //************************************************

    fun loadScreen(playlistID: String , mainVM: MainVM){

        val playlist = playlistsQueries.getPlaylist(playlistID)

        val songs = mainVM.songsData.value?.songs

        val temp = ArrayList<SongInAddSongs>()

        val newSongs = ArrayList<SongInAddSongs>()

        songs!!.forEach { song->
            temp.add(SongInAddSongs(
                id = song.id,
                title = song.title,
                selected = false,
                albumID = song.albumID,
                artistID = song.artistID
            ))
        }

        temp.forEach {song->

            if(!playlist.songs.any { it == song.id }){
                newSongs.add(song)
            }
        }

        newSongs.sortBy { it.title }

        _songs.update { newSongs }

        _screenLoaded.update { true }
    }

    fun toggleSong(songID: Long){

        val newSongs = songs.value!!.toMutableList()

        val songIndex = songs.value!!.indexOfFirst { it.id == songID }

        newSongs[songIndex] = newSongs[songIndex].copy(selected = !newSongs[songIndex].selected)

        _songs.update { newSongs }
    }

    suspend fun addSongs(playlistID: String, onSuccess: () -> Unit, playlistVM: PlaylistScreenVM, mainVM: MainVM){

        val playlist = playlistsQueries.getPlaylist(playlistID)

        val playlistSongsIDS = playlist.songs.toMutableList()

        songs.value!!.forEach {song->
            if(song.selected){
                playlistSongsIDS.add(song.id)
            }
        }

        playlistsQueries.updatePlaylistSongs(playlist, playlistSongsIDS.toList())

        val newSongs = ArrayList<Song>()

        val songs = mainVM.songsData.value?.songs

        songs!!.forEach {song->
            playlistSongsIDS.forEach { playlistSongID->
                if(playlistSongID == song.id){
                    newSongs.add(song)
                }
            }
        }

        playlistVM.updateSongs(newSongs)
        playlistVM.updateCurrentSongs(newSongs)

        onSuccess()
    }

    fun clearScreen(){

        _screenLoaded.value = false
        _songs.update { null }
    }
}