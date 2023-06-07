package com.lighttigerxiv.simple.mp.compose.screens.main.artists

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.Routes
import com.lighttigerxiv.simple.mp.compose.data.variables.Sorts
import com.lighttigerxiv.simple.mp.compose.screens.main.artist.ArtistScreenVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ArtistsScreenVM(application: Application) : AndroidViewModel(application) {

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

    private val _currentArtists = MutableStateFlow<List<Song>?>(null)
    val currentArtists = _currentArtists.asStateFlow()
    fun updateCurrentArtists(newValue: List<Song>?){
        _currentArtists.update { newValue }
    }

    private val _recentArtists = MutableStateFlow<List<Song>?>(null)
    val recentArtists = _recentArtists.asStateFlow()

    private val _oldestArtists = MutableStateFlow<List<Song>?>(null)
    val oldestArtists = _oldestArtists.asStateFlow()

    private val _ascendentArtists = MutableStateFlow<List<Song>?>(null)
    val ascendentArtists = _ascendentArtists.asStateFlow()

    private val _descendentArtists = MutableStateFlow<List<Song>?>(null)
    val descendentArtists = _descendentArtists.asStateFlow()


    //************************************************
    // Functions
    //************************************************


    fun loadScreen(mainVM: MainVM) {

        val sortType = preferences.getString("ArtistsSortType", Sorts.RECENT)
        val songs = mainVM.songs.value

        if (songs != null) {

            val artists = songs.distinctBy { it.artistID }

            _recentArtists.update { artists }

            _oldestArtists.update { artists.reversed() }

            _ascendentArtists.update { artists.sortedBy { it.artist } }

            _descendentArtists.update { artists.sortedByDescending { it.artist } }


            _currentArtists.update {
                when (sortType) {
                    Sorts.RECENT -> recentArtists.value
                    Sorts.OLDEST -> oldestArtists.value
                    Sorts.ASCENDENT -> ascendentArtists.value
                    else -> descendentArtists.value
                }
            }

            _screenLoaded.update { true }
        }
    }

    fun filterArtists() {

        when (preferences.getString("ArtistsSortType", Sorts.RECENT)) {

            Sorts.RECENT -> _currentArtists.update { recentArtists.value!!.filter { it.artist.lowercase().trim().contains(searchText.value.lowercase().trim()) } }
            Sorts.OLDEST -> _currentArtists.update { oldestArtists.value!!.filter { it.artist.lowercase().trim().contains(searchText.value.lowercase().trim()) } }
            Sorts.ASCENDENT -> _currentArtists.update { ascendentArtists.value!!.filter { it.artist.lowercase().trim().contains(searchText.value.lowercase().trim()) } }
            Sorts.DESCENDENT -> _currentArtists.update { descendentArtists.value!!.filter { it.artist.lowercase().trim().contains(searchText.value.lowercase().trim()) } }
        }
    }

    fun updateSortType(sortType: String) {

        preferences.edit().putString("ArtistsSortType", sortType).apply()
    }

    fun openArtist(activityContext: ViewModelStoreOwner, navController: NavHostController , id: Long){
        ViewModelProvider(activityContext)[ArtistScreenVM::class.java].clearScreen()
        navController.navigate("${Routes.MAIN.ARTIST}${id}")
    }
}