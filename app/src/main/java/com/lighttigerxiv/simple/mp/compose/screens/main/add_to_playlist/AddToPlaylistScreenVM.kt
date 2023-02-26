package com.lighttigerxiv.simple.mp.compose.screens.main.add_to_playlist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.lighttigerxiv.simple.mp.compose.Song
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.MainVM
import com.lighttigerxiv.simple.mp.compose.data.mongodb.getMongoRealm
import com.lighttigerxiv.simple.mp.compose.data.mongodb.items.Playlist
import com.lighttigerxiv.simple.mp.compose.data.mongodb.queries.PlaylistsQueries
import com.lighttigerxiv.simple.mp.compose.toMongoHex
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.mongodb.kbson.ObjectId

class AddToPlaylistScreenVM(application: Application) : AndroidViewModel(application) {

    //************************************************
    // Variables
    //************************************************

    val context = application

    private val playlistsQueries = PlaylistsQueries(getMongoRealm())

    private val _screenLoaded = MutableStateFlow(false)
    val screenLoaded = _screenLoaded.asStateFlow()

    private val _playlists = MutableStateFlow<List<Playlist>?>(null)
    val playlists = _playlists.asStateFlow()

    private val _playlistNameText = MutableStateFlow("")
    val playlistNameText = _playlistNameText.asStateFlow()
    fun updatePlaylistNameText(newValue: String) {
        _playlistNameText.update { newValue }
    }

    //************************************************
    // Functions
    //************************************************


    fun loadScreen() {

        _playlists.update { playlistsQueries.getPlaylists() }

        _screenLoaded.update { true }
    }

    fun createPlaylist() {

        playlistsQueries.createPlaylist(playlistNameText.value)

        _playlists.update { playlistsQueries.getPlaylists() }
    }

    suspend fun addSong(songID: Long, playlist: Playlist, onSuccess: () -> Unit, onError: () -> Unit) {

        val newSongs = ArrayList<Long>().apply {
            add(songID)
        }


        if (playlist.songs.any { it == songID }) {

            onError()
        } else {

            playlist.songs.forEach {
                newSongs.add(it)
            }

            playlistsQueries.updatePlaylistSongs(playlist, newSongs)

            onSuccess()
        }
    }

    fun clearScreen() {
        _screenLoaded.update { false }
        _playlistNameText.update { "" }
    }
}