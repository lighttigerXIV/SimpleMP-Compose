package com.lighttigerxiv.simple.mp.compose.screens.main.add_song_to_playlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lighttigerxiv.simple.mp.compose.data.mongodb.getMongoRealm
import com.lighttigerxiv.simple.mp.compose.data.mongodb.items.Playlist
import com.lighttigerxiv.simple.mp.compose.data.mongodb.queries.PlaylistsQueries
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.PlaylistsScreenVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddSongToPlaylistScreenVM(application: Application) : AndroidViewModel(application) {

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

    fun createPlaylist(playlistsVM: PlaylistsScreenVM) {

        playlistsQueries.createPlaylist(playlistNameText.value)

        _playlists.update { playlistsQueries.getPlaylists() }

        _playlistNameText.update { "" }

        playlistsVM.updatePlaylists(playlistsQueries.getPlaylists())

        playlistsVM.updateCurrentPlaylists(playlistsQueries.getPlaylists().filter {
            it.name.trim().lowercase().contains(playlistsVM.searchText.value.trim().lowercase())
        })
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