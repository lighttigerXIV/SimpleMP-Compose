package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library

import android.app.Application
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
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
    application: Application,
    libraryRepository: LibraryRepository,
    playbackRepository: PlaybackRepository
) : ViewModel() {

    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SimpleMPApplication)
                val libraryRepository = application.container.libraryRepository
                val playbackRepository = application.container.playbackRepository

                LibraryScreenVM(application, libraryRepository, playbackRepository)
            }
        }
    }

    data class UiState(
        val peekHeight: Dp,
        val currentSong: Song?,
        val hideNavBarProgress: Float,
        val showPlayerProgress: Float,
        val hideMiniPlayer: Boolean
    )


    private val _uiState = MutableStateFlow(
        UiState(
            peekHeight = 0.dp,
            currentSong = null,
            hideNavBarProgress = 0f,
            showPlayerProgress = 0f,
            hideMiniPlayer = false
        )
    )
    val uiState = _uiState.asStateFlow()


    init {
        if (!libraryRepository.initialized.value) {
            viewModelScope.launch(Dispatchers.Main) {
                libraryRepository.initLibrary(application)
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.currentSong.collect { newSong ->
                _uiState.update {
                    uiState.value.copy(
                        currentSong = newSong,
                        peekHeight = if (newSong != null) 125.dp else 0.dp
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    fun updateNavbarAnimation(progress: Float, sheetState: BottomSheetState){
        viewModelScope.launch(Dispatchers.Main) {

            if (progress > 0 && sheetState.currentValue == BottomSheetValue.Collapsed && sheetState.targetValue == BottomSheetValue.Expanded) {
                if (!uiState.value.hideMiniPlayer) {
                    _uiState.update { uiState.value.copy(hideMiniPlayer = true) }
                }
            }

            if (progress >= 0.9f && sheetState.targetValue == BottomSheetValue.Collapsed) {
                if (uiState.value.hideMiniPlayer) {
                    _uiState.update { uiState.value.copy(hideMiniPlayer = false) }
                }
            }

            if (progress == 1f && sheetState.targetValue == BottomSheetValue.Expanded) {
                _uiState.update { uiState.value.copy(hideMiniPlayer = true) }
            }


            if (progress in 0.1f..1f) {
                if (sheetState.targetValue == BottomSheetValue.Expanded) {
                    _uiState.update { uiState.value.copy(hideNavBarProgress = 0 + progress, showPlayerProgress = 0 + progress) }
                } else {
                    _uiState.update { uiState.value.copy( hideNavBarProgress = 1 - progress, showPlayerProgress = 1 - progress) }
                }
            }
        }
    }
}