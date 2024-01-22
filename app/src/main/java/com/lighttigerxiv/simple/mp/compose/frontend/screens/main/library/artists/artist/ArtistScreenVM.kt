package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.artists.artist

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.gson.Gson
import com.lighttigerxiv.simple.mp.compose.SimpleMPApplication
import com.lighttigerxiv.simple.mp.compose.backend.realm.Queries
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Album
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.backend.realm.getRealm
import com.lighttigerxiv.simple.mp.compose.backend.repositories.InternalStorageRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaybackRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.SettingsRepository
import com.lighttigerxiv.simple.mp.compose.backend.requests.DiscogsResponse
import com.lighttigerxiv.simple.mp.compose.backend.requests.getDiscogsRetrofit
import com.lighttigerxiv.simple.mp.compose.backend.utils.compressed
import com.lighttigerxiv.simple.mp.compose.backend.utils.isNetworkAvailable
import com.lighttigerxiv.simple.mp.compose.backend.utils.isOnMobileData
import com.lighttigerxiv.simple.mp.compose.backend.utils.isOnWifi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.toHexString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ArtistScreenVM(
    private val application: Application,
    private val libraryRepository: LibraryRepository,
    private val playbackRepository: PlaybackRepository,
    private val internalStorageRepository: InternalStorageRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val app = (this[APPLICATION_KEY] as SimpleMPApplication)

                ArtistScreenVM(
                    app,
                    app.container.libraryRepository,
                    app.container.playbackRepository,
                    app.container.internalStorageRepository,
                    app.container.settingsRepository
                )
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
        val showMenu: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val queries = Queries(getRealm())

    var pagerTab = 0
    var songsPosition = 0
    var albumsPosition = 0

    fun loadScreen(artistId: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            settingsRepository.settingsFlow.collect { settings ->

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

                viewModelScope.launch(Dispatchers.Main) {
                    libraryRepository.artistImageRequests.collect {
                        updateArtistImage(artistId)
                    }
                }

                viewModelScope.launch(Dispatchers.Main) {
                    libraryRepository.onArtistImageChanged = {
                        updateArtistImage(artistId)
                    }
                }

                val request = libraryRepository.getArtistImageRequest(artistId)

                if (request == null) {
                    if (settings.downloadArtistCover && isNetworkAvailable(application)) {
                        if (isOnWifi(application) || (settings.downloadArtistCoverWithData && isOnMobileData(application))) {

                            getDiscogsRetrofit()
                                .getArtistCover(
                                    token = "Discogs token=addIURHUBwvyDlSqWcNqPWkHXUbMgUzNgbpZGZnd",
                                    artist = libraryRepository.getArtistName(artistId)
                                )
                                .enqueue(object : Callback<String> {
                                    override fun onResponse(call: Call<String>, response: Response<String>) {
                                        viewModelScope.launch(Dispatchers.Main) {
                                            try {
                                                val jsonData = Gson().fromJson(response.body(), DiscogsResponse::class.java)

                                                if (jsonData.results.isNotEmpty()) {

                                                    val imageUrl = jsonData.results[0].cover_image

                                                    if (imageUrl.endsWith(".gif")) {

                                                        queries.addArtistImageRequest(artistId)
                                                        libraryRepository.loadArtistImageRequests()

                                                    } else {

                                                        val imageRequest = ImageRequest.Builder(application)
                                                            .data(imageUrl)
                                                            .target { drawable ->
                                                                viewModelScope.launch(Dispatchers.Main) {

                                                                    val artistArt = drawable.toBitmap().compressed()

                                                                    internalStorageRepository.saveImageToInternalStorage(artistId.toHexString(), artistArt)

                                                                    queries.addArtistImageRequest(artistId)
                                                                    libraryRepository.loadArtistImageRequests()
                                                                }
                                                            }
                                                            .build()

                                                        ImageLoader(application).enqueue(imageRequest)
                                                    }
                                                }

                                            } catch (e: Exception) {
                                                Log.e("Discogs Response Error", e.message.toString())
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<String>, t: Throwable) {
                                        Log.e("Discogs API", t.message.toString())
                                    }
                                })
                        }
                    }
                }
            }
        }
    }

    fun updateArtistImage(artistId: Long) {
        val request = libraryRepository.getArtistImageRequest(artistId)

        if (request == null) {
            _uiState.update { uiState.value.copy(artistImage = null) }
        } else {

            if (request.useDefault) {
                _uiState.update { uiState.value.copy(artistImage = null) }
            } else {
                viewModelScope.launch(Dispatchers.Main) {
                    internalStorageRepository.loadImageFromInternalStorage(artistId.toHexString()).collect { image ->
                        _uiState.update { uiState.value.copy(artistImage = image) }
                    }
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

    fun shuffle() {
        playbackRepository.shuffleAndPlay(uiState.value.songs)
    }

    fun updateShowMenu(v: Boolean) {
        _uiState.update { uiState.value.copy(showMenu = v) }
    }
}