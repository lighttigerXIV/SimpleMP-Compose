package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.artists.artist

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lighttigerxiv.simple.mp.compose.SimpleMPApplication
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Album
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaybackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ArtistScreenVM(
    private val libraryRepository: LibraryRepository,
    private val playbackRepository: PlaybackRepository
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val application = (this[APPLICATION_KEY] as SimpleMPApplication)
                val libraryRepository = application.container.libraryRepository
                val playbackRepository = application.container.playbackRepository

                ArtistScreenVM(libraryRepository, playbackRepository)
            }
        }
    }

    data class UiState(
        val loadingRequested: Boolean = false,
        val isLoading: Boolean = true,
        val artistImage: Bitmap? = null,
        val artistName: String = "",
        val songs: List<Song> = ArrayList(),
        val albums: List<Album> = ArrayList(),
        val currentSong: Song? = null,
        val currentPagerTab: Int = 0,
        val showMenu: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun loadScreen(artistId: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            _uiState.update { uiState.value.copy(loadingRequested = true) }

            _uiState.update {
                uiState.value.copy(
                    artistName = libraryRepository.getArtistName(artistId),
                    songs = libraryRepository.getArtistSongs(artistId),
                    albums = libraryRepository.getArtistAlbums(artistId),
                    isLoading = false
                )
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.currentSongState.collect { newSongState ->
                _uiState.update {
                    uiState.value.copy(currentSong = newSongState?.currentSong)
                }
            }
        }
    }

    fun getAlbumArt(albumId: Long): Bitmap? {
        return libraryRepository.getLargeAlbumArt(albumId = albumId)
    }

    fun playSong(song: Song) {
        playbackRepository.playSelectedSong(song, uiState.value.songs)
    }

    fun updateCurrentPagerTab(v: Int) {
        _uiState.update { uiState.value.copy(currentPagerTab = v) }
    }

    fun shuffle(){
        playbackRepository.shuffleAndPlay(uiState.value.songs)
    }

    fun updateShowMenu(v: Boolean){
        _uiState.update { uiState.value.copy(showMenu = v) }
    }
}