package com.lighttigerxiv.simple.mp.compose.screens.main.artist.artist_select_cover

import android.app.Application
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.mongodb.getMongoRealm
import com.lighttigerxiv.simple.mp.compose.data.mongodb.queries.ArtistsCoversQueries
import com.lighttigerxiv.simple.mp.compose.data.responses.DiscogsResponse
import com.lighttigerxiv.simple.mp.compose.data.variables.Settings
import com.lighttigerxiv.simple.mp.compose.functions.isNetworkAvailable
import com.lighttigerxiv.simple.mp.compose.functions.isOnMobileData
import com.lighttigerxiv.simple.mp.compose.retrofit.getDiscogsRetrofit
import com.lighttigerxiv.simple.mp.compose.screens.main.artist.ArtistScreenVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectArtistCoverScreenVM(application: Application) : AndroidViewModel(application) {

    //************************************************
    // Variables
    //************************************************

    private val context = application

    private val artistsCoversQueries = ArtistsCoversQueries(getMongoRealm())

    private val _screenLoaded = MutableStateFlow(false)
    val screenLoaded = _screenLoaded.asStateFlow()

    private val _covers = MutableStateFlow<List<DiscogsResponse.Result>?>(null)
    val covers = _covers.asStateFlow()

    private var artistID: Long = 0

    private var artistVM: ArtistScreenVM? = null

    private val preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    private val _showOnlineResults = MutableStateFlow(preferences.getBoolean(Settings.DOWNLOAD_COVER, false))
    val showOnlineResults = _showOnlineResults.asStateFlow()

    //************************************************
    // Functions
    //************************************************

    fun loadScreen(artistName: String, id: Long, artVM: ArtistScreenVM) {

        artistID = id

        artistVM = artVM

        val canDownloadCover = preferences.getBoolean(Settings.DOWNLOAD_COVER, false)

        val canDownloadOverData = preferences.getBoolean(Settings.DOWNLOAD_OVER_DATA, false)

        val isInternetAvailable = isNetworkAvailable(context)

        val isMobileDataEnabled = isOnMobileData(context)

        if (canDownloadCover && isInternetAvailable) {

            if ((isMobileDataEnabled && canDownloadOverData) || !isMobileDataEnabled) {

                getDiscogsRetrofit()
                    .getArtistCover(
                        token = "Discogs token=addIURHUBwvyDlSqWcNqPWkHXUbMgUzNgbpZGZnd",
                        artist = artistName
                    )
                    .enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {

                            if (response.code() == 200) {

                                val data = Gson().fromJson(response.body(), DiscogsResponse::class.java)

                                _covers.update { data.copy(results = data.results.filter { !it.cover_image.endsWith(".gif") }).results }
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {

                            Log.e("Discogs Error", "Error while getting ")
                        }
                    })
            }
        }

        _screenLoaded.update { true }
    }

    fun updateArtistCover(bitmapString: String) {

        runBlocking {
            withContext(Dispatchers.IO) {

                artistsCoversQueries.updateArtistCover(artistID, bitmapString)

                val artist = artistsCoversQueries.getArtist(artistID)

                val imageBytes = Base64.decode(artist!!.image, Base64.DEFAULT)

                artistVM!!.updateTintCover(false)

                artistVM!!.updateArtistCover(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size))
            }
        }

        onGoBack()
    }

    fun clearArtistCover() {

        runBlocking {
            withContext(Dispatchers.IO) {

                artistsCoversQueries.updateArtistCover(artistID, null)

                artistVM!!.updateTintCover(true)

                artistVM!!.updateArtistCover(BitmapFactory.decodeResource(context.resources, R.drawable.person))
            }
        }

        onGoBack()
    }

    fun clearScreen() {

        _screenLoaded.update { false }
        _covers.update { null }
    }

    //************************************************
    // Callbacks
    //************************************************

    var onGoBack: () -> Unit = {}
}