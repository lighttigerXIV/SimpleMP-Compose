package com.lighttigerxiv.simple.mp.compose.screens.main.floating_artist

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.lighttigerxiv.simple.mp.compose.*
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM
import com.lighttigerxiv.simple.mp.compose.data.mongodb.getMongoRealm
import com.lighttigerxiv.simple.mp.compose.data.mongodb.queries.ArtistsCoversQueries
import com.lighttigerxiv.simple.mp.compose.data.responses.DiscogsResponse
import com.lighttigerxiv.simple.mp.compose.functions.isNetworkAvailable
import com.lighttigerxiv.simple.mp.compose.functions.isOnMobileData
import com.lighttigerxiv.simple.mp.compose.retrofit.getDiscogsRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class FloatingArtistScreenVM(application: Application) : AndroidViewModel(application) {

    //************************************************
    // Variables
    //************************************************

    private val context = application

    private val artistsCoversQueries = ArtistsCoversQueries(getMongoRealm())

    private val _screenLoaded = MutableStateFlow(false)
    val screenLoaded = _screenLoaded.asStateFlow()

    private val _artistName = MutableStateFlow("")
    val artistName = _artistName.asStateFlow()

    private val _artistCover = MutableStateFlow(BitmapFactory.decodeResource(context.resources, R.drawable.person_hd))
    val artistCover = _artistCover.asStateFlow()


    private val _tintCover = MutableStateFlow(true)
    val tintCover = _tintCover.asStateFlow()


    private val _artistSongs = MutableStateFlow<List<Song>?>(null)
    val artistSongs = _artistSongs.asStateFlow()


    //************************************************
    // Functions
    //************************************************

    fun loadScreen(artistID: Long, mainVM: MainVM, settingsVM: SettingsVM) {

        val songsData = mainVM.songsData.value
        val songs = songsData?.songs
        val artists = songsData?.artists

        if(songs != null && artists != null){

            val artist = artists.first { it.id == artistID }

            _artistName.update { artist.name }

            var artistQuery = artistsCoversQueries.getArtist(artistID)

            if (artistQuery == null) {

                artistsCoversQueries.addArtist(artistID)
            }

            artistQuery = artistsCoversQueries.getArtist(artistID)

            _artistSongs.update { songs.filter { it.artistID == artistID } }

            _screenLoaded.update { true }

            //Loads Artist Image
            if (artistQuery!!.alreadyRequested) {

                if (artistQuery.image != null) {

                    val imageBytes = Base64.decode(artistQuery.image, Base64.DEFAULT)

                    _tintCover.update { false }

                    _artistCover.update { BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size) }
                }
            } else {

                val canDownloadArtistCover = settingsVM.downloadArtistCoverSetting.value
                val isInternetAvailable = isNetworkAvailable(context)
                val canDownloadOverData = settingsVM.downloadOverDataSetting.value
                val isMobileDataEnabled = isOnMobileData(context)

                if (isInternetAvailable && canDownloadArtistCover) {
                    if ((canDownloadOverData && isMobileDataEnabled) || (!canDownloadOverData && !isMobileDataEnabled)) {
                        getDiscogsRetrofit()
                            .getArtistCover(
                                token = "Discogs token=addIURHUBwvyDlSqWcNqPWkHXUbMgUzNgbpZGZnd",
                                artist = artistName.value
                            )
                            .enqueue(object : Callback<String> {
                                override fun onResponse(call: Call<String>, response: Response<String>) {
                                    if (response.code() == 200) {

                                        try {

                                            val data = Gson().fromJson(response.body(), DiscogsResponse::class.java)

                                            if (data.results.isNotEmpty()) {

                                                val imageUrl = data.results[0].cover_image

                                                if (imageUrl.endsWith(".gif")) {

                                                    runBlocking {
                                                        withContext(Dispatchers.IO) {
                                                            artistsCoversQueries.updateArtistCover(artistID, null)
                                                            artistsCoversQueries.updateArtistAlreadyRequested(artistID)
                                                        }
                                                    }

                                                } else {
                                                    Glide.with(context)
                                                        .asBitmap()
                                                        .load(imageUrl)
                                                        .into(object : CustomTarget<Bitmap>() {
                                                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                                                                val baos = ByteArrayOutputStream()

                                                                resource.compress(Bitmap.CompressFormat.PNG, 50, baos)

                                                                val b = baos.toByteArray()

                                                                val imageString = Base64.encodeToString(b, Base64.DEFAULT)

                                                                runBlocking {
                                                                    withContext(Dispatchers.IO) {
                                                                        artistsCoversQueries.updateArtistCover(artistID, imageString)
                                                                        artistsCoversQueries.updateArtistAlreadyRequested(artistID)
                                                                    }
                                                                }

                                                                _tintCover.update { false }

                                                                _artistCover.update { resource }
                                                            }

                                                            override fun onLoadCleared(placeholder: Drawable?) {}
                                                        })
                                                }
                                            }
                                        } catch (exc: Exception) {

                                            Toast.makeText(context, exc.message.toString(), Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Log.e("Discogs Error", "Error while getting artist cover")
                                }
                            })
                    }
                }
            }
        }
    }

    fun clearScreen() {

        _screenLoaded.update { false }
        _artistName.update { "" }
        _artistCover.update { BitmapFactory.decodeResource(context.resources, R.drawable.person_hd) }
        _tintCover.update { true }
        _artistSongs.update { null }
    }
}