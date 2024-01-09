package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library

import android.graphics.Bitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryScreenVM(
    private val libraryRepository: LibraryRepository,
    private val playbackRepository: PlaybackRepository
) : ViewModel() {

    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SimpleMPApplication)

                LibraryScreenVM(
                    application.container.libraryRepository,
                    application.container.playbackRepository
                )
            }
        }
    }

    data class UiState(
        val peekHeight: Dp = 0.dp,
        val currentSong: Song? = null,
        val showMiniPlayer: Boolean = false,
        val smallAlbumArt: Bitmap? = null,
        val currentSongArtistName: String = "",
        val isPlaying: Boolean = false
    )


    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()


    init {
        if (!libraryRepository.initialized.value) {
            viewModelScope.launch(Dispatchers.Main) {
                libraryRepository.initLibrary()
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.currentSongState.collect { newSongState ->
                _uiState.update {
                    uiState.value.copy(
                        currentSong = newSongState?.currentSong,
                        peekHeight = if (newSongState != null) 125.dp else 0.dp,
                        smallAlbumArt = if (newSongState?.currentSong != null) libraryRepository.getSmallAlbumArt(newSongState.currentSong.albumId) else null,
                        currentSongArtistName = if (newSongState?.currentSong != null) libraryRepository.getArtistName(newSongState.currentSong.artistId) else "",
                        showMiniPlayer = newSongState?.currentSong != null
                    )
                }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.playbackState.collect { newPlaybackState ->
                _uiState.update { uiState.value.copy(isPlaying = newPlaybackState?.isPlaying ?: false) }
            }
        }
    }

    fun pauseOrResume() {
        playbackRepository.pauseResume()
    }
}