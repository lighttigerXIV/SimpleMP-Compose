package com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.AndroidViewModel
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.Song
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.MainVM
import com.lighttigerxiv.simple.mp.compose.data.mongodb.getMongoRealm
import com.lighttigerxiv.simple.mp.compose.data.mongodb.items.Playlist
import com.lighttigerxiv.simple.mp.compose.data.mongodb.queries.PlaylistsQueries
import com.lighttigerxiv.simple.mp.compose.getBitmapFromVectorDrawable
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.PlaylistsScreenVM
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class PlaylistScreenVM(application: Application) : AndroidViewModel(application) {

    //************************************************
    // Variables
    //************************************************

    val context = application

    private val playlistsQueries = PlaylistsQueries(getMongoRealm())

    private val _screenLoaded = MutableStateFlow(false)
    val screenLoaded = _screenLoaded.asStateFlow()

    private val _playlistImage = MutableStateFlow<ImageBitmap?>(null)
    val playlistImage = _playlistImage.asStateFlow()

    private val _playlist = MutableStateFlow<Playlist?>(null)
    val playlist = _playlist.asStateFlow()

    private val _songs = MutableStateFlow<List<Song>?>(null)
    val songs = _songs.asStateFlow()

    private val _currentSongs = MutableStateFlow<List<Song>?>(null)
    val currentSongs = _currentSongs.asStateFlow()

    private val _showMenu = MutableStateFlow(false)
    val showMenu = _showMenu.asStateFlow()
    fun updateShowMenu(newValue: Boolean) {
        _showMenu.update { newValue }
    }

    private val _showSelectImageDialog = MutableStateFlow(false)
    val showSelectImageDialog = _showSelectImageDialog.asStateFlow()
    fun updateShowSelectImageDialog(newValue: Boolean) {
        _showSelectImageDialog.update { newValue }
    }

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog = _showDeleteDialog.asStateFlow()
    fun updateShowDeleteDialog(newValue: Boolean) {
        _showDeleteDialog.update { newValue }
    }

    private val _onEditMode = MutableStateFlow(false)
    val onEditMode = _onEditMode.asStateFlow()
    fun updateOnEditMode(newValue: Boolean) {
        _onEditMode.update { newValue }
    }

    private val _playlistNameText = MutableStateFlow("")
    val playlistNameText = _playlistNameText.asStateFlow()
    fun updatePlaylistNameText(newValue: String) {
        _playlistNameText.update { newValue }
    }

    private val _saveButtonEnabled = MutableStateFlow(true)
    val saveButtonEnabled= _saveButtonEnabled.asStateFlow()
    fun updateSaveButtonEnabled(newValue:Boolean) {
        _saveButtonEnabled.update { newValue }
    }

    //************************************************
    // Functions
    //************************************************


    fun loadScreen(
        playlistID: String,
        mainVM: MainVM,
        playlistsVM: PlaylistsScreenVM
    ) {

        val playlists = playlistsVM.playlists.value

        if (playlists != null) {

            _playlist.update { playlistsQueries.getPlaylist(playlistID) }

            if (playlist.value != null) {

                _playlistNameText.update { playlist.value!!.name }

                _playlistImage.update {
                    getBitmapFromVectorDrawable(context, R.drawable.playlist).asImageBitmap()
                }

                val songsIDS = playlist.value!!.songs
                val newSongs = ArrayList<Song>()

                songsIDS.forEach { songID ->
                    if(mainVM.songs.value!!.any { it.id == songID }){
                        newSongs.add(mainVM.songs.value!!.first { it.id == songID })
                    }
                }

                _songs.update { newSongs }
                _currentSongs.update { newSongs }

                Log.d("Playlist songs", playlist.value?.songs.toString())

                _screenLoaded.update { true }
            }
        }
    }

    suspend fun deletePlaylist(playlistID: String, playlistsVM: PlaylistsScreenVM) {

        playlistsQueries.deletePlaylist(playlistID)

        playlistsVM.updatePlaylists(playlistsQueries.getPlaylists())
        playlistsVM.updateCurrentPlaylists(
            playlistsVM.playlists.value!!.filter { it.name.lowercase().trim().contains(playlistsVM.searchText.value.lowercase().trim()) }
        )
    }

    fun removeSong(song: Song){

        _currentSongs.update { songs.value!!.filter { it.id != song.id } }
    }

    suspend fun savePlaylistChanges(playlistsVM: PlaylistsScreenVM){

        val newSongs = ArrayList<Long>()

        currentSongs.value!!.forEach {
            newSongs.add(it.id)
        }

        val updatedPlaylist = Playlist().apply {
            _id = playlist.value!!._id
            name = playlistNameText.value
            image = null
            songs = newSongs.toRealmList()
        }


        playlistsQueries.updatePlaylist(updatedPlaylist)

        _playlist.value = updatedPlaylist

        playlistsVM.updatePlaylists(playlistsQueries.getPlaylists())

        playlistsVM.updateCurrentPlaylists(
            playlistsQueries.getPlaylists().filter {
                it.name.lowercase().trim().contains(playlistsVM.searchText.value.lowercase().trim())
            }
        )
    }

    fun clearScreen() {

        _screenLoaded.update { false }
        _playlist.update { null }
        _songs.update { null }
        _currentSongs.update { null }
        _showMenu.update { false }
        _showDeleteDialog.update { false }
        _showSelectImageDialog.update { false }
    }
}