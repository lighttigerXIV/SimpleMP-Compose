package com.lighttigerxiv.simple.mp.compose.backend.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lighttigerxiv.simple.mp.compose.backend.realm.Queries
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Album
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Artist
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.backend.realm.getRealm
import com.lighttigerxiv.simple.mp.compose.backend.settings.Settings
import com.lighttigerxiv.simple.mp.compose.backend.settings.SettingsOptions
import com.lighttigerxiv.simple.mp.compose.backend.settings.SettingsRepository
import com.lighttigerxiv.simple.mp.compose.backend.utils.hasNotificationsPermission
import com.lighttigerxiv.simple.mp.compose.backend.utils.hasStoragePermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppVM(application: Application) : AndroidViewModel(application) {

    private val context = application

    private val queries = Queries(getRealm())

    private val _initialized = MutableStateFlow(false)
    val initialized = _initialized.asStateFlow()

    private val _songs = MutableStateFlow<List<Song>?>(null)
    val song = _songs.asStateFlow()

    private val _artists = MutableStateFlow<List<Artist>?>(null)
    val artists = _artists.asStateFlow()

    private val _albums = MutableStateFlow<List<Album>?>(null)
    val albums = _albums.asStateFlow()


    fun refreshLibrary(){
        viewModelScope.launch(Dispatchers.Main){
            _songs.update { queries.getSongs() }
            _albums.update { queries.getAlbums() }
            _artists.update { queries.getArtists() }
        }
    }

    init {

        if (hasStoragePermission(context) && hasNotificationsPermission(context)) {

            _songs.update { queries.getSongs() }
            _albums.update { queries.getAlbums() }
            _artists.update { queries.getArtists() }

            _initialized.update { true }

        } else {

            _initialized.update { true }
        }
    }
}