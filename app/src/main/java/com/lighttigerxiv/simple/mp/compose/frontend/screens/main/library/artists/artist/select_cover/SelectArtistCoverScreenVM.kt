package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.artists.artist.select_cover

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.gson.Gson
import com.lighttigerxiv.simple.mp.compose.SimpleMPApplication
import com.lighttigerxiv.simple.mp.compose.backend.realm.Queries
import com.lighttigerxiv.simple.mp.compose.backend.realm.getRealm
import com.lighttigerxiv.simple.mp.compose.backend.repositories.InternalStorageRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.SettingsRepository
import com.lighttigerxiv.simple.mp.compose.backend.requests.DiscogsResponse
import com.lighttigerxiv.simple.mp.compose.backend.requests.getDiscogsRetrofit
import com.lighttigerxiv.simple.mp.compose.backend.utils.canDownloadArtistImages
import com.lighttigerxiv.simple.mp.compose.backend.utils.compressed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.toHexString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectArtistCoverScreenVM(
    private val application: Application,
    private val internalStorageRepository: InternalStorageRepository,
    private val settingsRepository: SettingsRepository,
    private val libraryRepository: LibraryRepository
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as SimpleMPApplication)

                SelectArtistCoverScreenVM(
                    app,
                    app.container.internalStorageRepository,
                    app.container.settingsRepository,
                    app.container.libraryRepository
                )
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = true,
        val requestedLoading: Boolean = false,
        val canDownloadImages: Boolean = false,
        val imagesUrls: List<String> = ArrayList()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val queries = Queries(getRealm())

    fun load(artistId: Long) {
        viewModelScope.launch(Dispatchers.Main) {

            _uiState.update { uiState.value.copy(requestedLoading = true) }

            settingsRepository.settingsFlow.collect { settings ->

                _uiState.update {
                    uiState.value.copy(
                        isLoading = false,
                        canDownloadImages = canDownloadArtistImages(settings, application)
                    )
                }

                if (canDownloadArtistImages(settings, application)) {
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
                                        _uiState.update { uiState.value.copy(imagesUrls = jsonData.results.filter { !it.cover_image.endsWith(".gif") }.map { it.cover_image }) }

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

    fun changeArtistImage(artistImage: Bitmap?, artistId: Long) {
        artistImage?.let {
            viewModelScope.launch(Dispatchers.Main) {
                internalStorageRepository.saveImageToInternalStorage(artistId.toHexString(), artistImage.compressed())
                queries.addArtistImageRequest(artistId)
                libraryRepository.loadArtistImageRequests()
            }
        }
    }

    fun clearArtistImage(artistId: Long){
        viewModelScope.launch(Dispatchers.Main) {
            queries.defaultArtistImageRequest(artistId)
            libraryRepository.loadArtistImageRequests()
        }
    }
}