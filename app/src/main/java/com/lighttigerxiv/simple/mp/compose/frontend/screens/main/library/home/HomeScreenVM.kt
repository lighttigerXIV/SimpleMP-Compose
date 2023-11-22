package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.home

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lighttigerxiv.simple.mp.compose.SimpleMPApplication
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaybackRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HomeUiState(
    val isLoading: Boolean,
    val songs: List<Song>,
    val albumArts: List<LibraryRepository.AlbumArt>,
    val searchText: String,
    val showMenu: Boolean,
    val currentSong: Song?
)

class HomeScreenVM(
    private val application: Application,
    private val libraryRepository: LibraryRepository,
    private val playbackRepository: PlaybackRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SimpleMPApplication)
                val libraryRepository = application.container.libraryRepository
                val playbackRepository = application.container.playbackRepository
                val settingsRepository = application.container.settingsRepository

                HomeScreenVM(application, libraryRepository, playbackRepository, settingsRepository)
            }
        }
    }

    init {

        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.currentSongState.collect { newSongState ->
                _uiState.update {
                    uiSate.value.copy(currentSong = newSongState?.currentSong)
                }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            libraryRepository.smallAlbumArts.collect { arts ->
                _uiState.update {
                    uiSate.value.copy(albumArts = arts)
                }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            libraryRepository.songs.collect { songs ->
                librarySongs = songs
                filterSongs()
            }
        }
    }

    private var librarySongs: List<Song> = ArrayList()

    private val _uiState = MutableStateFlow(
        HomeUiState(
            isLoading = true,
            songs = ArrayList(),
            albumArts = ArrayList(),
            searchText = "",
            currentSong = null,
            showMenu = false
        )
    )
    val uiSate = _uiState.asStateFlow()

    private fun filterSongs() {
        val newSongs = librarySongs.filter { song -> song.name.trim().lowercase().contains(uiSate.value.searchText.trim().lowercase()) }
        _uiState.update { uiSate.value.copy(songs = newSongs) }
    }

    fun updateSearchText(v: String) {
        _uiState.update { uiSate.value.copy(searchText = v) }
        filterSongs()
    }

    fun updateShowMenu(v: Boolean) {
        _uiState.update { uiSate.value.copy(showMenu = v) }
    }

    fun getSongArt(albumId: Long): Bitmap? {
        return libraryRepository.getSmallAlbumArt(albumId)
    }

    fun getArtistName(artistId: Long): String {
        return libraryRepository.getArtistName(artistId)
    }

    fun playSong(song: Song) {
        playbackRepository.playSelectedSong(song, uiSate.value.songs)
    }

    fun shuffleAndPlay(){
        playbackRepository.shuffleAndPlay(librarySongs)
    }
}