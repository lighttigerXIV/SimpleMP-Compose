package com.lighttigerxiv.simple.mp.compose.screens.main.playlists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.MainVM
import com.lighttigerxiv.simple.mp.compose.data.AppDatabase
import com.lighttigerxiv.simple.mp.compose.data.Playlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlaylistsScreenVM(application: Application) : AndroidViewModel(application) {

    //************************************************
    // Variables
    //************************************************

    val context = application

    private val playlistDao = AppDatabase.getInstance(application).playlistDao

    private val _screenLoaded = MutableStateFlow(false)
    val screenLoaded = _screenLoaded.asStateFlow()

    private val _genres = MutableStateFlow<List<String>?>(null)
    val genres = _genres.asStateFlow()

    private val _playlists = MutableStateFlow<List<Playlist>?>(null)
    val playlists = _playlists.asStateFlow()

    private val _currentPlaylists = MutableStateFlow<List<Playlist>?>(null)
    val currentPlaylists = _currentPlaylists.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    fun updateSearchText(newValue:String) {
        _searchText.update { newValue }
    }

    private val _playlistNameText = MutableStateFlow("")
    val playlistNameText = _playlistNameText.asStateFlow()
    fun updatePlaylistNameText(newValue:String) {
        _playlistNameText.update { newValue }
    }


    //************************************************
    // Function
    //************************************************

    fun loadScreen(mainVM: MainVM){

        val songs = mainVM.songs.value
        val newGenres = ArrayList<String>()

        if(songs != null){

            songs.forEach {
                newGenres.add(it.genre)
            }

            _genres.update { newGenres.distinctBy { it } }

            _playlists.update { playlistDao.getPlaylists() }

            _currentPlaylists.update { playlists.value }

            _screenLoaded.update { true }
        }
    }

    fun createPlaylist(name: String) {

        playlistDao.insertPlaylist(
            Playlist(name = name)
        )

        _playlists.update { playlistDao.getPlaylists() }
        _currentPlaylists.update { playlists.value }
    }
}