package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.albums.album

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lighttigerxiv.simple.mp.compose.SimpleMPApplication
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaybackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlbumScreenVM(
    private val libraryRepository: LibraryRepository,
    private val playbackRepository: PlaybackRepository
): ViewModel() {

    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SimpleMPApplication)
                val libraryRepository = application.container.libraryRepository
                val playbackRepository = application.container.playbackRepository

                AlbumScreenVM(libraryRepository, playbackRepository)
            }
        }
    }

    data class UiState(
        val loadingRequested: Boolean = false,
        val isLoading: Boolean = true,
        val largeAlbumArt: Bitmap? = null,
        val smallAlbumArt: Bitmap? = null,
        val albumName: String = "",
        val artistName: String = "",
        val songs: List<Song> = ArrayList(),
        val currentSong: Song? = null,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    var listPosition = 0

    fun loadScreen(albumId: Long){
        viewModelScope.launch(Dispatchers.Main) {
            _uiState.update { uiState.value.copy(loadingRequested = true) }

            val album = libraryRepository.albums.value.first { it.id == albumId }

            _uiState.update {
                uiState.value.copy(
                    albumName = album.name,
                    artistName = libraryRepository.getArtistName(album.artistId),
                    largeAlbumArt = libraryRepository.getLargeAlbumArt(albumId),
                    smallAlbumArt = libraryRepository.getSmallAlbumArt(albumId),
                    songs = libraryRepository.getAlbumSongs(albumId),
                    isLoading = false
                )
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.currentSongState.collect{newSongState->
                _uiState.update { uiState.value.copy(currentSong = newSongState?.currentSong) }
            }
        }
    }

    fun playSong(song: Song){
        playbackRepository.playSelectedSong(song, uiState.value.songs)
    }

    fun shuffle(){
        playbackRepository.shuffleAndPlay(uiState.value.songs)
    }
}