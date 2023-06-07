package com.lighttigerxiv.simple.mp.compose.screens.main.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.Sorts
import com.lighttigerxiv.simple.mp.compose.functions.unaccent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeScreenVM(application: Application) : AndroidViewModel(application) {


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

    private val _currentSongs = MutableStateFlow<List<Song>?>(null)
    val currentSongs = _currentSongs.asStateFlow()
    fun updateCurrentSongs(newValue: List<Song>?) {
        _currentSongs.update { newValue }
    }

    private val _recentSongs = MutableStateFlow<List<Song>?>(null)
    val recentSongs = _recentSongs.asStateFlow()

    private val _oldestSongs = MutableStateFlow<List<Song>?>(null)
    val oldestSongs = _oldestSongs.asStateFlow()

    private val _ascendentSongs = MutableStateFlow<List<Song>?>(null)
    val ascendentSongs = _ascendentSongs.asStateFlow()

    private val _descendentSongs = MutableStateFlow<List<Song>?>(null)
    val descendentSongs = _descendentSongs.asStateFlow()


    //************************************************
    // Functions
    //************************************************


    fun loadScreen(mainVM: MainVM) {

        val sortType = preferences.getString("HomeSongsSortType", Sorts.RECENT)
        val songs = mainVM.songs.value

        if (songs != null) {

            _recentSongs.update { songs }

            _oldestSongs.update { songs.reversed() }

            _ascendentSongs.update { songs.sortedBy { it.title } }

            _descendentSongs.update { songs.sortedByDescending { it.title } }

            _currentSongs.update {
                when (sortType) {
                    Sorts.RECENT -> recentSongs.value
                    Sorts.OLDEST -> oldestSongs.value
                    Sorts.ASCENDENT -> ascendentSongs.value
                    else -> descendentSongs.value
                }
            }

            _screenLoaded.update { true }
        }
    }

    fun filterSongs() {

        when (preferences.getString("HomeSongsSortType", Sorts.RECENT)) {

            Sorts.RECENT -> _currentSongs.update { recentSongs.value!!.filter { it.title.unaccent().lowercase().trim().contains(searchText.value.unaccent().lowercase().trim()) } }
            Sorts.OLDEST -> _currentSongs.update { oldestSongs.value!!.filter { it.title.unaccent().lowercase().trim().contains(searchText.value.unaccent().lowercase().trim()) } }
            Sorts.ASCENDENT -> _currentSongs.update { ascendentSongs.value!!.filter { it.title.unaccent().lowercase().trim().contains(searchText.value.unaccent().lowercase().trim()) } }
            Sorts.DESCENDENT -> _currentSongs.update { descendentSongs.value!!.filter { it.title.unaccent().lowercase().trim().contains(searchText.value.unaccent().lowercase().trim()) } }
        }
    }

    fun updateSortType(sortType: String) {

        preferences.edit().putString("HomeSongsSortType", sortType).apply()
    }

    fun selectSong(song: Song, mainVM: MainVM) {

        val newQueue = when (preferences.getString("HomeSongsSortType", Sorts.RECENT)) {

            Sorts.RECENT -> recentSongs.value!!
            Sorts.OLDEST -> oldestSongs.value!!
            Sorts.ASCENDENT -> ascendentSongs.value!!
            else -> descendentSongs.value!!
        }

        mainVM.selectSong(newQueue, newQueue.indexOf(song))
    }
}