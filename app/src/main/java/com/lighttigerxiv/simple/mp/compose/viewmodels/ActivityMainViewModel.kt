package com.lighttigerxiv.simple.mp.compose.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.*
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.IBinder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.lighttigerxiv.simple.mp.compose.*
import com.lighttigerxiv.simple.mp.compose.data.AppDatabase
import com.lighttigerxiv.simple.mp.compose.data.Playlist
import com.lighttigerxiv.simple.mp.compose.services.SimpleMPService

class ActivityMainViewModel(application: Application) : AndroidViewModel(application) {

    private val playlistDao = AppDatabase.getInstance(application).playlistDao

    val songsList = GetSongs.getSongsList(application, "Recent")
    var songsImagesList = GetSongs.getAllAlbumsImages(application)
    lateinit var navController: NavController

    //Home Songs
    var recentHomeSongsList = ArrayList(songsList)
    var oldestHomeSongsList = ArrayList(songsList)
    var ascendentHomeSongsList = ArrayList(songsList)
    var descendentHomeSongsList = ArrayList(songsList)

    //Artists Songs
    var recentArtistsList = ArrayList(songsList)
    var oldestArtistsList = ArrayList(songsList)
    var ascendentArtistsList = ArrayList(songsList)
    var descendentArtistsList = ArrayList(songsList)

    //Albums Songs
    var recentAlbumsList = ArrayList(songsList)
    var oldestAlbumsList = ArrayList(songsList)
    var ascendentAlbumsList = ArrayList(songsList)
    var descendentAlbumsList = ArrayList(songsList)

    //Genres Songs
    var genresList = ArrayList(recentHomeSongsList)

    var currentHomeSongsList = MutableLiveData(recentHomeSongsList)
    val currentArtistsList = MutableLiveData(recentHomeSongsList)
    val currentAlbumsList = MutableLiveData(recentHomeSongsList)

    val clickedArtistID = MutableLiveData<Long>(0)
    val clickedArtistAlbumID = MutableLiveData<Long>(0)
    val clickedAlbumID = MutableLiveData<Long>(0)
    val clickedGenreID = MutableLiveData<Long>(0)
    val clickedPlaylistID = MutableLiveData(0)


    val playlists = MutableLiveData(playlistDao.getAllPlaylists())
    val playlistSongs = MutableLiveData(ArrayList<Song>())
    val currentPlaylistSongs = MutableLiveData(ArrayList<Song>())


    var hintHomeSearchText = MutableLiveData("Search Songs")
    var homeSearchText = MutableLiveData("")

    var hintArtistsSearchText = MutableLiveData("Search Artists")
    var artistsSearchText = MutableLiveData("")

    var hintAlbumsSearchText = MutableLiveData("Search Albums")
    var albumsSearchText = MutableLiveData("")

    var surfaceColor = MutableLiveData<Color>()

    var tfNewPlaylistNameValue = MutableLiveData("")

    @SuppressLint("StaticFieldLeak")
    private lateinit var smpService: SimpleMPService
    private var isServiceBound = false


    //Screens states

    //Home Screen
    var showHomePopupMenu = MutableLiveData(false)

    //Playlist Screen
    var tfPlaylistName_PlaylistScreen = MutableLiveData("")
    var isOnEditMode_PlaylistScreen = MutableLiveData(false)


    //Callbacks
    var onSongSelected: (Song) -> Unit = {}
    var onSongSecondPassed: (position: Int) -> Unit = {}
    var onMediaPlayerStopped: () -> Unit = {}


    //Player Sates
    var currentMediaPlayerPosition = MutableLiveData(0)
    var isMusicShuffled = MutableLiveData(false)
    var isMusicOnRepeat = MutableLiveData(false)




    //Song Related
    var selectedSong = MutableLiveData<Song?>(null)
    var selectedSongTitle = MutableLiveData("")
    var selectedSongArtistName = MutableLiveData("")
    var selectedSongPath = MutableLiveData("")
    var selectedSongDuration = MutableLiveData(0)
    var selectedSongMinutesAndSeconds = MutableLiveData("")
    var selectedSongCurrentMinutesAndSeconds = MutableLiveData("")
    var selectedSongAlbumArt = MutableLiveData<Bitmap?>(null)

    //Mini Player UI
    var miniPlayerHeight = MutableLiveData(0.dp)
    private val miniPlayerPauseIcon = BitmapFactory.decodeResource(
        application.resources,
        R.drawable.icon_pause
    ).asImageBitmap()
    val miniPlayerPlayIcon = BitmapFactory.decodeResource(
        application.resources,
        R.drawable.icon_play
    ).asImageBitmap()
    var currentMiniPlayerIcon = MutableLiveData(miniPlayerPauseIcon)

    //Player UI
    val closePlayerIcon = UsefulFunctions.getBitmapFromVectorDrawable(application, R.drawable.icon_arrow_down_solid).asImageBitmap()
    val queueListIcon = BitmapFactory.decodeResource(
        application.resources,
        R.drawable.icon_queue_solid
    ).asImageBitmap()
    val shuffleIcon = BitmapFactory.decodeResource(
        application.resources,
        R.drawable.icon_shuffle_solid
    ).asImageBitmap()
    val previousIcon = BitmapFactory.decodeResource(
        application.resources,
        R.drawable.icon_previous_solid
    ).asImageBitmap()
    private val pauseRoundIcon = BitmapFactory.decodeResource(
        application.resources,
        R.drawable.icon_pause_round_solid
    ).asImageBitmap()
    private val playRoundIcon = BitmapFactory.decodeResource(
        application.resources,
        R.drawable.icon_play_round_solid
    ).asImageBitmap()
    val nextIcon = BitmapFactory.decodeResource(application.resources, R.drawable.icon_next_solid).asImageBitmap()
    val repeatIcon = BitmapFactory.decodeResource(
        application.resources,
        R.drawable.icon_repeat_solid
    ).asImageBitmap()

    var currentPlayerIcon = MutableLiveData(pauseRoundIcon)


    private val simpleMPConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            val binder = service as SimpleMPService.LocalBinder
            smpService = binder.getService()
            if (smpService.isMusicPlayingOrPaused()) {

                updatePlayPauseIcons()
                selectedSong.value = smpService.currentSong
                selectedSongTitle.value = selectedSong.value!!.title
                selectedSongArtistName.value = selectedSong.value!!.artistName
                selectedSongPath.value = selectedSong.value!!.path
                selectedSongDuration.value = selectedSong.value!!.duration
                currentMediaPlayerPosition.value = smpService.getCurrentMediaPlayerPosition()
                selectedSongMinutesAndSeconds.value = getMinutesAndSecondsFromPosition(selectedSongDuration.value!! / 1000)
                selectedSongCurrentMinutesAndSeconds.value = "0:00"
                updateCurrentSongAlbumArt()
                miniPlayerHeight.value = 60.dp

                isMusicShuffled.value = smpService.isMusicShuffled
                isMusicOnRepeat.value = smpService.isMusicOnRepeat
            }

            isMusicShuffled.value = smpService.isMusicShuffled
            isMusicOnRepeat.value = smpService.isMusicOnRepeat

            isServiceBound = true


            smpService.onSongSelected = { song ->

                selectedSong.value = smpService.currentSong
                selectedSongTitle.value = selectedSong.value!!.title
                selectedSongArtistName.value = selectedSong.value!!.artistName
                selectedSongPath.value = selectedSong.value!!.path
                selectedSongDuration.value = selectedSong.value!!.duration
                currentMediaPlayerPosition.value = smpService.getCurrentMediaPlayerPosition()
                selectedSongMinutesAndSeconds.value = getMinutesAndSecondsFromPosition(selectedSongDuration.value!! / 1000)
                selectedSongCurrentMinutesAndSeconds.value = "0:00"
                updateCurrentSongAlbumArt()

                isMusicShuffled.value = smpService.isMusicShuffled
                isMusicOnRepeat.value = smpService.isMusicOnRepeat

                currentPlayerIcon.value = pauseRoundIcon
                currentMiniPlayerIcon.value = miniPlayerPauseIcon

                onSongSelected(song)
            }


            smpService.onSongSecondPassed = { mediaPlayerPosition ->
                currentMediaPlayerPosition.value = mediaPlayerPosition
                selectedSongCurrentMinutesAndSeconds.value = getMinutesAndSecondsFromPosition(mediaPlayerPosition / 1000)
                onSongSecondPassed(mediaPlayerPosition / 1000)
            }

            smpService.onSongPaused = {

                currentMiniPlayerIcon.value = miniPlayerPlayIcon
                currentPlayerIcon.value = playRoundIcon
            }

            smpService.onSongResumed = {

                currentMiniPlayerIcon.value = miniPlayerPauseIcon
                currentPlayerIcon.value = pauseRoundIcon
            }

            smpService.onMediaPlayerStopped = {
                selectedSongPath.value = ""
                onMediaPlayerStopped()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {}
    }


    fun selectSong(newQueueList: ArrayList<Song>, position: Int) {

        if (newQueueList.size > 0) {

            smpService.selectSong(getApplication(), newQueueList, position)
            onSongSelected(smpService.currentSong!!)
            selectedSong.value = smpService.currentSong
            selectedSongTitle.value = selectedSong.value!!.title
            selectedSongArtistName.value = selectedSong.value!!.artistName
            selectedSongPath.value = selectedSong.value!!.path
            selectedSongDuration.value = selectedSong.value!!.duration
            selectedSongMinutesAndSeconds.value = getMinutesAndSecondsFromPosition(selectedSongDuration.value!! / 1000)
            selectedSongCurrentMinutesAndSeconds.value = "0:00"
            updateCurrentSongAlbumArt()
            currentMiniPlayerIcon.value = miniPlayerPauseIcon
            currentPlayerIcon.value = pauseRoundIcon
        }
    }


    fun shuffle(newQueueList: ArrayList<Song>) {

        smpService.shuffleAndPlay(newQueueList, getApplication())
        currentMiniPlayerIcon.value = miniPlayerPauseIcon
        currentPlayerIcon.value = pauseRoundIcon
    }


    fun seekSongPosition(position: Int) {
        if (isServiceBound) {
            smpService.seekTo(position)
        }
    }

    fun updateCurrentSongAlbumArt() {

        selectedSongAlbumArt.value = songsImagesList.find { it.albumID == selectedSong.value!!.albumID }!!.albumArt
    }

    fun toggleShuffle() {
        smpService.toggleShuffle()
        isMusicShuffled.value = smpService.isMusicShuffled
    }

    fun selectPreviousSong() {
        smpService.selectPreviousSong(getApplication())
    }

    fun selectNextSong() {
        smpService.selectNextSong(getApplication())
    }

    fun toggleRepeat() {
        smpService.toggleRepeat()
        isMusicOnRepeat.value = smpService.isMusicOnRepeat
    }


    fun getMinutesAndSecondsFromPosition(position: Int): String {

        val minutes = position.div(60)
        val seconds = position.rem(60)

        var stringSeconds = "$seconds"


        if (seconds < 10)
            stringSeconds = "0$seconds"


        return "$minutes:$stringSeconds"
    }

    fun updatePlayPauseIcons() {

        currentMiniPlayerIcon.value = when {
            smpService.isMusicPlaying() -> miniPlayerPauseIcon
            else -> miniPlayerPlayIcon
        }

        currentPlayerIcon.value = when {
            smpService.isMusicPlaying() -> pauseRoundIcon
            else -> playRoundIcon
        }
    }


    fun pauseResumeMusic() {
        if (isServiceBound) {

            smpService.pauseResumeMusic(getApplication())
            updatePlayPauseIcons()
        }
    }


    fun getIsMusicPaused(): Boolean{
        return if(isServiceBound) !smpService.isMusicPlaying() else false
    }


    init {

        val serviceIntent = Intent(application, SimpleMPService::class.java)
        application.bindService(serviceIntent, simpleMPConnection, Context.BIND_AUTO_CREATE)


        val artistsList = songsList.distinctBy { it.artistID }
        recentArtistsList = ArrayList(artistsList)
        oldestArtistsList = ArrayList(artistsList)
        ascendentArtistsList = ArrayList(artistsList)
        descendentArtistsList = ArrayList(artistsList)


        val albumsList = songsList.distinctBy { it.albumID }
        recentAlbumsList = ArrayList(albumsList)
        oldestAlbumsList = ArrayList(albumsList)
        ascendentAlbumsList = ArrayList(albumsList)
        descendentAlbumsList = ArrayList(albumsList)


        genresList = recentHomeSongsList.distinctBy { song -> song.genreID } as ArrayList<Song>


        //Sorts the songs
        val sharedPrefs = application.getSharedPreferences("sorting", MODE_PRIVATE)
        val homeSort = sharedPrefs.getString("home", "Recent")
        val artistsSort = sharedPrefs.getString("artists", "Recent")
        val albumsSort = sharedPrefs.getString("albums", "Recent")


        oldestHomeSongsList.reverse()
        ascendentHomeSongsList.sortBy { it.title }
        descendentHomeSongsList.sortByDescending { it.title }

        oldestArtistsList.reverse()
        ascendentArtistsList.sortBy { it.artistName }
        descendentArtistsList.sortByDescending { it.artistName }

        oldestAlbumsList.reverse()
        ascendentAlbumsList.sortBy { it.albumName }
        descendentAlbumsList.sortByDescending { it.albumName }


        when (homeSort) {

            "Recent" -> {
                currentHomeSongsList.value = recentHomeSongsList
            }
            "Oldest" -> {
                currentHomeSongsList.value = oldestHomeSongsList
            }
            "Ascendent" -> {
                currentHomeSongsList.value = ascendentHomeSongsList
            }
            "Descendent" -> {
                currentHomeSongsList.value = descendentHomeSongsList
            }
        }

        when (artistsSort) {

            "Recent" -> {
                currentArtistsList.value = recentArtistsList
            }
            "Oldest" -> {
                currentArtistsList.value = oldestArtistsList
            }
            "Ascendent" -> {
                currentArtistsList.value = ascendentArtistsList
            }
            "Descendent" -> {
                currentArtistsList.value = descendentArtistsList
            }
        }

        when (albumsSort) {

            "Recent" -> {
                currentAlbumsList.value = recentAlbumsList
            }
            "Oldest" -> {
                currentAlbumsList.value = oldestAlbumsList
            }
            "Ascendent" -> {
                currentAlbumsList.value = ascendentAlbumsList
            }
            "Descendent" -> {
                currentAlbumsList.value = descendentAlbumsList
            }
        }
    }


    fun createPlaylist(name: String) {

        playlistDao.insertPlaylist(
            Playlist(name = name)
        )

        playlists.value = playlistDao.getAllPlaylists()
    }


    fun deletePlaylist(playlistID: Int) {

        playlistDao.deletePlaylist(playlistID = playlistID)
        playlists.value = playlistDao.getAllPlaylists()
    }

    fun updatePlaylistName(playlistID: Int, playlistName: String) {

        playlistDao.updatePlaylistName(
            playlistName = playlistName,
            playlistID = playlistID
        )

        playlists.value = playlistDao.getAllPlaylists()
    }


    fun updatePlaylistSongs( songsJson : String, playlistID: Int ){

        playlistDao.updatePlaylistSongs(
            songsJson = songsJson,
            playlistID = playlistID
        )

        playlists.value = playlistDao.getAllPlaylists()
    }


    fun filterHomeSongsList(sortType: String) {

        when (sortType) {

            "Recent" -> currentHomeSongsList.value = recentHomeSongsList.filterNot { !it.title.lowercase().trim().contains(homeSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Oldest" -> currentHomeSongsList.value = oldestHomeSongsList.filterNot { !it.title.lowercase().trim().contains(homeSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Ascendent" -> currentHomeSongsList.value = ascendentHomeSongsList.filterNot { !it.title.lowercase().trim().contains(homeSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Descendent" -> currentHomeSongsList.value = descendentHomeSongsList.filterNot { !it.title.lowercase().trim().contains(homeSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
        }
    }

    fun filterArtistsList(sortType: String) {

        when (sortType) {

            "Recent" -> currentArtistsList.value = recentArtistsList.filterNot { !it.artistName.lowercase().trim().contains(artistsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Oldest" -> currentArtistsList.value = oldestArtistsList.filterNot { !it.artistName.lowercase().trim().contains(artistsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Ascendent" -> currentArtistsList.value = ascendentArtistsList.filterNot { !it.artistName.lowercase().trim().contains(artistsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Descendent" -> currentArtistsList.value = descendentArtistsList.filterNot { !it.artistName.lowercase().trim().contains(artistsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
        }
    }

    fun filterAlbumsList(sortType: String) {

        when (sortType) {

            "Recent" -> currentAlbumsList.value = recentAlbumsList.filterNot { !it.albumName.lowercase().trim().contains(albumsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Oldest" -> currentAlbumsList.value = oldestAlbumsList.filterNot { !it.albumName.lowercase().trim().contains(albumsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Ascendent" -> currentAlbumsList.value = ascendentAlbumsList.filterNot { !it.albumName.lowercase().trim().contains(albumsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Descendent" -> currentAlbumsList.value = descendentAlbumsList.filterNot { !it.albumName.lowercase().trim().contains(albumsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
        }
    }
}