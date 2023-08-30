package com.lighttigerxiv.simple.mp.compose.screens.main.albums

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Album
import com.lighttigerxiv.simple.mp.compose.data.variables.Settings
import com.lighttigerxiv.simple.mp.compose.functions.unaccent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private val _currentAlbums = MutableStateFlow<List<Album>?>(null)
    val currentAlbums = _currentAlbums.asStateFlow()
    fun updateCurrentAlbums(newValue: List<Album>?){
        _currentAlbums.update { newValue }
    }

    private val _recentAlbums = MutableStateFlow<List<Album>?>(null)
    val recentAlbums = _recentAlbums.asStateFlow()

    private val _oldestAlbums = MutableStateFlow<List<Album>?>(null)
    val oldestAlbums = _oldestAlbums.asStateFlow()

    private val _ascendentAlbums = MutableStateFlow<List<Album>?>(null)
    val ascendentAlbums = _ascendentAlbums.asStateFlow()

    private val _descendentAlbums = MutableStateFlow<List<Album>?>(null)
    val descendentAlbums = _descendentAlbums.asStateFlow()


    //************************************************
    // Functions
    //************************************************


    fun loadScreen(mainVM: MainVM) {

        fun load(){
            val sortType = preferences.getString(Settings.ALBUMS_SORT, Settings.Values.Sort.RECENT)
            val albums = mainVM.songsData.value?.albums

            if (albums != null) {

                _recentAlbums.update { albums }

                _oldestAlbums.update { albums.reversed() }

                _ascendentAlbums.update { albums.sortedBy { it.title } }

                _descendentAlbums.update { albums.sortedByDescending { it.title } }

                _currentAlbums.update {
                    when (sortType) {
                        Settings.Values.Sort.RECENT -> recentAlbums.value
                        Settings.Values.Sort.OLDEST -> oldestAlbums.value
                        Settings.Values.Sort.ASCENDENT -> ascendentAlbums.value
                        else -> descendentAlbums.value
                    }
                }

                _screenLoaded.update { true }
            }
        }

        load()

        viewModelScope.launch{
            withContext(Dispatchers.IO){
                mainVM.songsData.collect{
                    load()
                    filterAlbums()
                }
            }
        }
    }

    fun filterAlbums() {
        when (preferences.getString(Settings.ALBUMS_SORT, Settings.Values.Sort.RECENT)) {
            Settings.Values.Sort.RECENT -> _currentAlbums.update { recentAlbums.value!!.filter { it.title.unaccent().lowercase().trim().contains(searchText.value.unaccent().lowercase().trim()) } }
            Settings.Values.Sort.OLDEST -> _currentAlbums.update { oldestAlbums.value!!.filter { it.title.unaccent().lowercase().trim().contains(searchText.value.unaccent().lowercase().trim()) } }
            Settings.Values.Sort.ASCENDENT -> _currentAlbums.update { ascendentAlbums.value!!.filter { it.title.unaccent().lowercase().trim().contains(searchText.value.unaccent().lowercase().trim()) } }
            Settings.Values.Sort.DESCENDENT -> _currentAlbums.update { descendentAlbums.value!!.filter { it.title.unaccent().lowercase().trim().contains(searchText.value.unaccent().lowercase().trim()) } }
        }
    }

    fun updateSortType(sortType: String) {

        preferences.edit().putString(Settings.ALBUMS_SORT, sortType).apply()
    }
}