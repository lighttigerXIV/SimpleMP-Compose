package com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist

import android.app.Application
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.mongodb.getMongoRealm
import com.lighttigerxiv.simple.mp.compose.data.mongodb.items.Playlist
import com.lighttigerxiv.simple.mp.compose.data.mongodb.queries.PlaylistsQueries
import com.lighttigerxiv.simple.mp.compose.data.variables.ImageSizes
import com.lighttigerxiv.simple.mp.compose.data.variables.Routes
import com.lighttigerxiv.simple.mp.compose.functions.getImage
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.PlaylistsScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.add_songs.AddSongsScreenVM
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

    private val _tintImage = MutableStateFlow(false)
    val tintImage = _tintImage.asStateFlow()

    private val _playlist = MutableStateFlow<Playlist?>(null)
    val playlist = _playlist.asStateFlow()

    private val _songs = MutableStateFlow<List<Song>?>(null)
    val songs = _songs.asStateFlow()
    fun updateSongs(newValue: List<Song>) {
        _songs.update { newValue }
    }

    private val _currentSongs = MutableStateFlow<List<Song>?>(null)
    val currentSongs = _currentSongs.asStateFlow()
    fun updateCurrentSongs(newValue: List<Song>) {
        _currentSongs.update { newValue }
    }


    private val _showMenu = MutableStateFlow(false)
    val showMenu = _showMenu.asStateFlow()
    fun updateShowMenu(newValue: Boolean) {
        _showMenu.update { newValue }
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
    val saveButtonEnabled = _saveButtonEnabled.asStateFlow()
    fun updateSaveButtonEnabled(newValue: Boolean) {
        _saveButtonEnabled.update { newValue }
    }

    private var updateImage = false

    private var deleteImage = false

    private var newImageString: String? = null


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

                    if(playlist.value!!.image != null){

                        val imageBytes = Base64.decode(playlist.value!!.image, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()

                    } else{
                        getImage(context, R.drawable.playlist_filled, ImageSizes.LARGE).asImageBitmap()
                    }
                }

                if(playlist.value!!.image == null){

                    _tintImage.update { true }
                } else{

                    _tintImage.update { false }
                }


                val songsIDS = playlist.value!!.songs

                val newSongs = ArrayList<Song>()

                songsIDS.forEach { songID ->
                    if (mainVM.songsData.value?.songs!!.any { it.id == songID }) {
                        newSongs.add(mainVM.songsData.value?.songs!!.first { it.id == songID })
                    }
                }

                _songs.update { newSongs }
                _currentSongs.update { newSongs }

                _screenLoaded.update { true }
            }
        }
    }

    fun cancelEdit(){

        deleteImage = false

        updateImage = false

        newImageString = null

        _playlistImage.update {

            if(playlist.value!!.image != null){

                val imageBytes = Base64.decode(playlist.value!!.image, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()

            } else{
                getImage(context, R.drawable.playlist_filled, ImageSizes.LARGE).asImageBitmap()
            }
        }

        if(playlist.value!!.image == null){

            _tintImage.update { true }
        } else{

            _tintImage.update { false }
        }

        _currentSongs.update { songs.value }
    }

    suspend fun deletePlaylist(playlistID: String, playlistsVM: PlaylistsScreenVM) {

        playlistsQueries.deletePlaylist(playlistID)

        playlistsVM.updatePlaylists(playlistsQueries.getPlaylists())
        playlistsVM.updateCurrentPlaylists(
            playlistsVM.playlists.value!!.filter { it.name.lowercase().trim().contains(playlistsVM.searchText.value.lowercase().trim()) }
        )
    }

    fun removeSong(song: Song) {

        _currentSongs.update { currentSongs.value!!.filter { it.id != song.id } }
    }

    fun deleteImage(){

        updateImage = false

        deleteImage = true

        _playlistImage.update { getImage(context, R.drawable.playlist_filled, ImageSizes.LARGE).asImageBitmap() }

        _tintImage.update { true }
    }

    fun onImageReceived(bitmapString: String){

        updateImage = true

        deleteImage = false

        newImageString = bitmapString

        val imageBytes = Base64.decode(bitmapString, Base64.DEFAULT)

        val imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()

        _tintImage.update { false }

        _playlistImage.update { imageBitmap }
    }

    suspend fun savePlaylistChanges(playlistsVM: PlaylistsScreenVM) {

        val newSongs = ArrayList<Long>()

        currentSongs.value!!.forEach {
            newSongs.add(it.id)
        }

        val updatedPlaylist = Playlist().apply {
            _id = playlist.value!!._id
            name = playlistNameText.value
            image = if(updateImage && newImageString != null){
                newImageString
            }else if(deleteImage){
                null
            }else{
                playlist.value!!.image
            }
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

        deleteImage = false

        updateImage = false
    }

    fun openAddSongsScreen(activityContext: ViewModelStoreOwner, rootNavController: NavHostController, id: String){
        ViewModelProvider(activityContext)[AddSongsScreenVM::class.java].clearScreen()
        rootNavController.navigate("${Routes.Root.ADD_SONGS_TO_PLAYLIST}$id")
    }

    fun clearScreen() {

        _screenLoaded.update { false }
        _playlist.update { null }
        _songs.update { null }
        _currentSongs.update { null }
        _showMenu.update { false }
        _showDeleteDialog.update { false }
        _tintImage.update { false }
    }
}