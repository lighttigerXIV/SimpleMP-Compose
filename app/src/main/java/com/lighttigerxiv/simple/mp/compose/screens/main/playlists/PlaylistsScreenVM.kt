package com.lighttigerxiv.simple.mp.compose.screens.main.playlists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.mongodb.getMongoRealm
import com.lighttigerxiv.simple.mp.compose.data.mongodb.items.Playlist
import com.lighttigerxiv.simple.mp.compose.data.mongodb.queries.PlaylistsQueries
import com.lighttigerxiv.simple.mp.compose.data.variables.Routes
import com.lighttigerxiv.simple.mp.compose.functions.unaccent
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.PlaylistScreenVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.net.URLEncoder

class PlaylistsScreenVM(application: Application) : AndroidViewModel(application) {

    //************************************************
    // Variables
    //************************************************

    val context = application

    private val playlistsQueries = PlaylistsQueries(getMongoRealm())

    private val _screenLoaded = MutableStateFlow(false)
    val screenLoaded = _screenLoaded.asStateFlow()

    private val _genres = MutableStateFlow<List<String>?>(null)
    val genres = _genres.asStateFlow()

    private val _playlists = MutableStateFlow<List<Playlist>?>(null)
    val playlists = _playlists.asStateFlow()
    fun updatePlaylists(newValue: List<Playlist>){
        _playlists.update { newValue }
    }

    private val _currentPlaylists = MutableStateFlow<List<Playlist>?>(null)
    val currentPlaylists = _currentPlaylists.asStateFlow()
    fun updateCurrentPlaylists(newValue: List<Playlist>){
        _currentPlaylists.update { newValue }
    }

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    fun updateSearchText(newValue: String) {
        _searchText.update { newValue }
    }

    private val _playlistNameText = MutableStateFlow("")
    val playlistNameText = _playlistNameText.asStateFlow()
    fun updatePlaylistNameText(newValue: String) {
        _playlistNameText.update { newValue }
    }


    //************************************************
    // Function
    //************************************************

    fun loadScreen(mainVM: MainVM) {

        val songs = mainVM.songs.value
        val newGenres = ArrayList<String>()

        if (songs != null) {

            songs.forEach {
                newGenres.add(it.genre)
            }

            _genres.update { newGenres.distinctBy { it }.sortedBy { it } }

            _playlists.update { playlistsQueries.getPlaylists() }

            _currentPlaylists.update { playlists.value }

            _screenLoaded.update { true }
        }
    }

    fun createPlaylist() {

        playlistsQueries.createPlaylist(playlistNameText.value)

        _playlists.update { playlistsQueries.getPlaylists() }
        _currentPlaylists.update { playlists.value }
    }

    fun filterPlaylists() {

        if (playlists.value != null) {

            _currentPlaylists.update {
                playlists.value!!.filter {
                    it.name.unaccent().lowercase().trim().contains(searchText.value.unaccent().lowercase().trim())
                }
            }
        }
    }

    fun openGenrePlaylist(navController: NavHostController, genre: String){
        val encodedGenre = URLEncoder.encode(genre, "UTF-8")
        navController.navigate("${Routes.Main.GENRE_PLAYLIST}$encodedGenre")
    }

    fun openPlaylist(activityContext: ViewModelStoreOwner, navController: NavHostController, id: String){
        ViewModelProvider(activityContext)[PlaylistScreenVM::class.java].clearScreen()
        navController.navigate("${Routes.Main.PLAYLIST}${id}")
    }
}