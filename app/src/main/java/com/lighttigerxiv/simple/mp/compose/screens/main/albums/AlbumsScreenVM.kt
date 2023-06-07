package com.lighttigerxiv.simple.mp.compose.screens.main.albums

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.Sorts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AlbumsScreenVM(application: Application) : AndroidViewModel(application) {


    //************************************************
    // Variables
    //************************************************


    val context = application

    private val preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    private val _screenLoaded = MutableStateFlow(false)
    val screenLoaded = _screenLoaded.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    fun updateSearchText(newValue: String) {
        _searchText.update { newValue }
    }

    private val _menuExpanded = MutableStateFlow(false)
    val menuExpanded = _menuExpanded.asStateFlow()
    fun updateMenuExpanded(newValue: Boolean) {
        _menuExpanded.update { newValue }
    }

    private val _currentAlbums = MutableStateFlow<List<Song>?>(null)
    val currentAlbums = _currentAlbums.asStateFlow()
    fun updateCurrentAlbums(newValue: List<Song>?){
        _currentAlbums.update { newValue }
    }

    private val _recentAlbums = MutableStateFlow<List<Song>?>(null)
    val recentAlbums = _recentAlbums.asStateFlow()

    private val _oldestAlbums = MutableStateFlow<List<Song>?>(null)
    val oldestAlbums = _oldestAlbums.asStateFlow()

    private val _ascendentAlbums = MutableStateFlow<List<Song>?>(null)
    val ascendentAlbums = _ascendentAlbums.asStateFlow()

    private val _descendentAlbums = MutableStateFlow<List<Song>?>(null)
    val descendentAlbums = _descendentAlbums.asStateFlow()


    //************************************************
    // Functions
    //************************************************


    fun loadScreen(mainVM: MainVM) {

        val sortType = preferences.getString("AlbumsSortType", Sorts.RECENT)
        val songs = mainVM.songs.value

        if (songs != null) {

            val albums = songs.distinctBy { it.albumID }

            _recentAlbums.update { albums }

            _oldestAlbums.update { albums.reversed() }

            _ascendentAlbums.update { albums.sortedBy { it.album } }

            _descendentAlbums.update { albums.sortedByDescending { it.album } }

            _currentAlbums.update {
                when (sortType) {
                    Sorts.RECENT -> recentAlbums.value
                    Sorts.OLDEST -> oldestAlbums.value
                    Sorts.ASCENDENT -> ascendentAlbums.value
                    else -> descendentAlbums.value
                }
            }

            _screenLoaded.update { true }
        }
    }

    fun filterAlbums() {

        when (preferences.getString("AlbumsSortType", Sorts.RECENT)) {

            Sorts.RECENT -> _currentAlbums.update { recentAlbums.value!!.filter { it.album.lowercase().trim().contains(searchText.value.lowercase().trim()) } }
            Sorts.OLDEST -> _currentAlbums.update { oldestAlbums.value!!.filter { it.album.lowercase().trim().contains(searchText.value.lowercase().trim()) } }
            Sorts.ASCENDENT -> _currentAlbums.update { ascendentAlbums.value!!.filter { it.album.lowercase().trim().contains(searchText.value.lowercase().trim()) } }
            Sorts.DESCENDENT -> _currentAlbums.update { descendentAlbums.value!!.filter { it.album.lowercase().trim().contains(searchText.value.lowercase().trim()) } }
        }
    }

    fun updateSortType(sortType: String) {

        preferences.edit().putString("AlbumsSortType", sortType).apply()
    }
}