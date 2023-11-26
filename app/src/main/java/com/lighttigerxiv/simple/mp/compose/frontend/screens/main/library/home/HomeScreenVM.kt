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
import com.lighttigerxiv.simple.mp.compose.backend.settings.SettingsOptions
import com.lighttigerxiv.simple.mp.compose.backend.utils.matchesSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HomeUiState(
    val isLoading: Boolean = true,
    val songs: List<Song> = ArrayList(),
    val albumArts: List<LibraryRepository.AlbumArt> = ArrayList(),
    val searchText: String = "",
    val showMenu: Boolean = false,
    val currentSong: Song? = null,
    val indexingLibrary: Boolean = false,
    val sortType: String = ""
)

class HomeScreenVM(
    private val application: Application,
    private val libraryRepository: LibraryRepository,
    private val playbackRepository: PlaybackRepository,
    private val settingsRepository: SettingsRepository
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
            settingsRepository.settingsFlow.collect { newSettings ->

                _uiState.update {
                    uiSate.value.copy(sortType = newSettings.homeSort)
                }

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
                        librarySongs = songs.sorted(newSettings.homeSort)
                        filterSongs()
                    }
                }

                viewModelScope.launch(Dispatchers.Main) {
                    libraryRepository.indexingLibrary.collect { newIndexingLIbrary ->
                        _uiState.update { uiSate.value.copy(indexingLibrary = newIndexingLIbrary) }
                    }
                }
            }
        }
    }

    private var librarySongs: List<Song> = ArrayList()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiSate = _uiState.asStateFlow()

    private fun filterSongs() {
        val newSongs = librarySongs.filter { song -> matchesSearch(song.name, uiSate.value.searchText) }
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

    fun shuffleAndPlay() {
        playbackRepository.shuffleAndPlay(librarySongs)
    }

    fun indexLibrary() {
        viewModelScope.launch(Dispatchers.Main) {
            libraryRepository.indexLibrary(application)
        }
    }

    fun updateSort(sortType: String) {
        viewModelScope.launch(Dispatchers.Main) {
            settingsRepository.updateHomeSort(sortType)

            librarySongs = libraryRepository.songs.value.sorted(sortType)
            filterSongs()
        }
    }

    private fun List<Song>.sorted(sortType: String): List<Song> {
        return when (sortType) {
            SettingsOptions.Sort.DEFAULT_REVERSED -> this.reversed()
            SettingsOptions.Sort.MODIFICATION_DATE_RECENT -> this.sortedByDescending { song -> song.modificationDate }
            SettingsOptions.Sort.MODIFICATION_DATE_OLD -> this.sortedByDescending { song -> song.modificationDate }.reversed()
            SettingsOptions.Sort.YEAR_RECENT -> this.sortedByDescending { song -> song.releaseYear }
            SettingsOptions.Sort.YEAR_OLD -> this.sortedBy { song -> song.releaseYear }
            SettingsOptions.Sort.ALPHABETICALLY_ASCENDENT -> this.sortedBy { song -> song.name }
            SettingsOptions.Sort.ALPHABETICALLY_DESCENDENT -> this.sortedByDescending { song -> song.name }
            else -> this
        }
    }
}