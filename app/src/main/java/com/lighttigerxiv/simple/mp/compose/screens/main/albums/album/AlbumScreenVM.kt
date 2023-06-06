package com.lighttigerxiv.simple.mp.compose.screens.main.albums.album

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AlbumScreenVM(application: Application) : AndroidViewModel(application) {

    //************************************************
    // Variables
    //************************************************


    val context = application

    private val _screenLoaded = MutableStateFlow(false)
    val screenLoaded = _screenLoaded.asStateFlow()

    private val _albumArt = MutableStateFlow<Bitmap?>(null)
    val albumArt = _albumArt.asStateFlow()

    private val _albumName = MutableStateFlow("")
    val albumName = _albumName.asStateFlow()

    private val _artistName = MutableStateFlow("")
    val artistName = _artistName.asStateFlow()

    private val _albumSongs = MutableStateFlow<List<Song>?>(null)
    val albumSongs = _albumSongs.asStateFlow()


    //************************************************
    // Functions
    //************************************************


    fun loadScreen(mainVM: MainVM, albumID: Long) {

        val songs = mainVM.songs.value
        val songsImages = mainVM.songsCovers.value



        _albumArt.update { songsImages?.find { it.albumID == albumID }?.albumArt }

        _albumSongs.update { songs?.filter { it.albumID == albumID } }

        _albumName.update { albumSongs.value!![0].album }

        _artistName.update { albumSongs.value!![0].artist }

        _screenLoaded.update { true }

    }

    fun clearScreen() {

        _albumArt.update { null }

        _albumName.update { "" }

        _artistName.update { "" }

        _albumSongs.update { null }

        _screenLoaded.update { false }
    }
}