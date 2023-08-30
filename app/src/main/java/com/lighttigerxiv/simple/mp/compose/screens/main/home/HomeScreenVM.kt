package com.lighttigerxiv.simple.mp.compose.screens.main.home

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.Settings
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.functions.unaccent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeScreenVM(application: Application) : AndroidViewModel(application) {

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

    private val _syncingSongs = MutableStateFlow(false)
    val syncingSongs= _syncingSongs.asStateFlow()

    fun loadScreen(mainVM: MainVM) {

        fun load(){
            val sortType = preferences.getString(Settings.HOME_SORT, Settings.Values.Sort.RECENT)
            val songs = mainVM.songsData.value?.songs

            if (songs != null) {

                _recentSongs.update { songs }

                _oldestSongs.update { songs.reversed() }

                _ascendentSongs.update { songs.sortedBy { it.title } }

                _descendentSongs.update { songs.sortedByDescending { it.title } }

                _currentSongs.update {
                    when (sortType) {
                        Settings.Values.Sort.RECENT -> recentSongs.value
                        Settings.Values.Sort.OLDEST -> oldestSongs.value
                        Settings.Values.Sort.ASCENDENT -> ascendentSongs.value
                        else -> descendentSongs.value
                    }
                }

                _screenLoaded.update { true }
            }
        }

        load()

        //Reloads the songs in case
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                mainVM.songsData.collect{
                    load()
                    filterSongs()
                }
            }
        }
    }

    fun filterSongs() {

        when (preferences.getString(Settings.HOME_SORT, Settings.Values.Sort.RECENT)) {

            Settings.Values.Sort.RECENT -> _currentSongs.update { recentSongs.value!!.filter { it.title.unaccent().lowercase().trim().contains(searchText.value.unaccent().lowercase().trim()) } }
            Settings.Values.Sort.OLDEST -> _currentSongs.update { oldestSongs.value!!.filter { it.title.unaccent().lowercase().trim().contains(searchText.value.unaccent().lowercase().trim()) } }
            Settings.Values.Sort.ASCENDENT -> _currentSongs.update { ascendentSongs.value!!.filter { it.title.unaccent().lowercase().trim().contains(searchText.value.unaccent().lowercase().trim()) } }
            Settings.Values.Sort.DESCENDENT -> _currentSongs.update { descendentSongs.value!!.filter { it.title.unaccent().lowercase().trim().contains(searchText.value.unaccent().lowercase().trim()) } }
        }
    }

    fun updateSortType(sortType: String) {

        preferences.edit().putString(Settings.HOME_SORT, sortType).apply()
    }

    fun selectSong(song: Song, mainVM: MainVM) {

        val newQueue = when (preferences.getString(Settings.HOME_SORT, Settings.Values.Sort.RECENT)) {

            Settings.Values.Sort.RECENT -> recentSongs.value!!
            Settings.Values.Sort.OLDEST -> oldestSongs.value!!
            Settings.Values.Sort.ASCENDENT -> ascendentSongs.value!!
            else -> descendentSongs.value!!
        }

        mainVM.selectSong(newQueue, newQueue.indexOf(song))
    }

    fun reloadSongs(mainVM: MainVM){

        Toast.makeText(context, getAppString(context, R.string.IndexingSongs), Toast.LENGTH_LONG).show()

        viewModelScope.launch {
            withContext(Dispatchers.IO){

                _menuExpanded.update { false }

                _syncingSongs.update { true }

                mainVM.indexSongs(showNotification = true, onFinish = {
                    _syncingSongs.update { false }
                })
            }
        }
    }
}