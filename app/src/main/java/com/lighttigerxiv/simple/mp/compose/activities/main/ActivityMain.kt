package com.lighttigerxiv.simple.mp.compose.activities.main

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.activities.setup.ActivityFirstSetup
import com.lighttigerxiv.simple.mp.compose.data.variables.ImageSizes
import com.lighttigerxiv.simple.mp.compose.data.variables.Routes
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.functions.getImage
import com.lighttigerxiv.simple.mp.compose.functions.getSurfaceColor
import com.lighttigerxiv.simple.mp.compose.screens.main.about.AboutScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.add_song_to_playlist.AddToPlaylistScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.add_song_to_playlist.AddSongToPlaylistScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.artist.artist_select_cover.SelectArtistCoverScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.floating_album.FloatingAlbumScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.floating_album.FloatingAlbumScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.floating_artist.FloatingArtistScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.floating_artist.FloatingArtistScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.main.MainScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.PlaylistsScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.PlaylistScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.add_songs.AddSongsScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.add_songs.AddSongsScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.settings.SettingsScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.settings.SettingsScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.settings.themes.ThemesScreen
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHeightSpacer
import com.lighttigerxiv.simple.mp.compose.ui.theme.ComposeSimpleMPTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {

    private val activityContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!getSharedPreferences(packageName, MODE_PRIVATE).getBoolean("setupCompleted", false)) {

            startActivity(
                Intent(applicationContext, ActivityFirstSetup::class.java)
            )

            finish()
        }

        createNotificationChannel()


        val vm = ViewModelProvider(this)[MainVM::class.java]
        val settingsVM = ViewModelProvider(this)[SettingsVM::class.java]



        setContent {
            ComposeSimpleMPTheme(
                useDarkTheme = isSystemInDarkTheme(),
                themeMode = settingsVM.themeModeSetting.collectAsState().value,
                themeAccent = settingsVM.themeAccentSetting.collectAsState().value,
                content = {

                    vm.updateSurfaceColor(getSurfaceColor(settingsVM = settingsVM))

                    val rootNavController = rememberNavController()
                    val loadingSongs = vm.loadingSongs.collectAsState().value
                    val context = LocalContext.current


                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(vm.surfaceColor.collectAsState().value)
                    ) {
                        if(loadingSongs){

                            rememberSystemUiController().setStatusBarColor(vm.surfaceColor.collectAsState().value)

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(vm.surfaceColor.collectAsState().value),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {


                                Image(
                                    modifier = Modifier
                                        .width(200.dp)
                                        .height(200.dp),
                                    bitmap = remember{ getImage(context,R.drawable.play_empty, ImageSizes.LARGE).asImageBitmap() },
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                )

                                SmallHeightSpacer()

                                LinearProgressIndicator(
                                    modifier = Modifier.width(200.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            }

                        } else{

                            NavHost(
                                modifier = Modifier
                                    .fillMaxSize(),
                                navController = rootNavController,
                                startDestination = Routes.ROOT.MAIN
                            ) {

                                composable(Routes.ROOT.MAIN) {

                                    MainScreen(
                                        mainVM = vm,
                                        activityContext = activityContext,
                                        rootNavController = rootNavController,
                                        onGetPlaylistImage = {

                                            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                                addCategory(Intent.CATEGORY_OPENABLE)
                                                type = "image/*"
                                                addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                                                addFlags(FLAG_GRANT_READ_URI_PERMISSION)
                                            }

                                            startActivityForResult(intent, 2)
                                        },
                                        onGetArtistCover = {

                                            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                                addCategory(Intent.CATEGORY_OPENABLE)
                                                type = "image/*"
                                                addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                                                addFlags(FLAG_GRANT_READ_URI_PERMISSION)
                                            }

                                            startActivityForResult(intent, 1)
                                        }
                                    )
                                }

                                composable("${Routes.ROOT.FLOATING_ARTIST}{id}") {
                                    val id = it.arguments?.getString("id")?.toLongOrNull()

                                    if (id != null) {
                                        FloatingArtistScreen(
                                            mainVM = vm,
                                            settingsVM = ViewModelProvider(activityContext)[SettingsVM::class.java],
                                            artistID = id,
                                            vm = ViewModelProvider(activityContext)[FloatingArtistScreenVM::class.java],
                                            onBackClicked = {
                                                rootNavController.navigateUp()
                                            }
                                        )
                                    }
                                }

                                composable("${Routes.ROOT.FLOATING_ALBUM}{albumID}") {

                                    val albumID = it.arguments?.getString("albumID")?.toLongOrNull()

                                    albumID?.let {

                                        FloatingAlbumScreen(
                                            mainVM = vm,
                                            albumVM = ViewModelProvider(activityContext)[FloatingAlbumScreenVM::class.java],
                                            albumID = albumID,
                                            onBackClicked = { rootNavController.navigateUp() }
                                        )
                                    }
                                }

                                composable(Routes.ROOT.SETTINGS) {

                                    SettingsScreen(
                                        mainVM = vm,
                                        settingsVM = ViewModelProvider(activityContext)[SettingsVM::class.java],
                                        settingsScreenVM = ViewModelProvider(activityContext)[SettingsScreenVM::class.java],
                                        onBackPressed = { rootNavController.navigateUp() },
                                        onOpenScreen = { rootNavController.navigate(it) }
                                    )
                                }

                                composable(Routes.ROOT.ABOUT) {

                                    AboutScreen(
                                        mainVM = vm,
                                        onBackClick = { rootNavController.navigateUp() }
                                    )
                                }

                                composable(Routes.ROOT.THEMES) {

                                    ThemesScreen(
                                        mainVM = vm,
                                        settingsVM = ViewModelProvider(activityContext)[SettingsVM::class.java],
                                        onBackClick = { rootNavController.navigateUp() }
                                    )
                                }

                                composable("${Routes.ROOT.ADD_SONGS_TO_PLAYLIST}{id}") {

                                    val id = it.arguments?.getString("id")

                                    id?.let {

                                        AddSongsScreen(
                                            mainVM = vm,
                                            addSongsVM = ViewModelProvider(activityContext)[AddSongsScreenVM::class.java],
                                            playlistVM = ViewModelProvider(activityContext)[PlaylistScreenVM::class.java],
                                            playlistID = id,
                                            onGoBack = {

                                                rootNavController.navigateUp()
                                            }
                                        )
                                    }
                                }

                                composable("${Routes.ROOT.ADD_SONG_TO_PLAYLIST}{id}") {

                                    val id = it.arguments?.getString("id")?.toLongOrNull()

                                    id?.let {
                                        AddToPlaylistScreen(
                                            mainVM = vm,
                                            songID = id,
                                            playlistsVM = ViewModelProvider(activityContext)[PlaylistsScreenVM::class.java],
                                            addToPlaylistVM = ViewModelProvider(activityContext)[AddSongToPlaylistScreenVM::class.java],
                                            previousPage = "Home",
                                            onBackClick = { rootNavController.navigateUp() }
                                        )
                                    }
                                }
                            }

                            rememberSystemUiController().apply {
                                setStatusBarColor(vm.surfaceColor.collectAsState().value)
                                setNavigationBarColor(MaterialTheme.colorScheme.surfaceVariant)
                            }

                        }
                    }
                }
            )
        }
    }

    private fun createNotificationChannel() {

        val channelName = getString(R.string.notificationChannelName)
        val channelDescription = getString(R.string.notificationChannelDescription)
        val channelImportance = NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel("Playback", channelName, channelImportance)
        mChannel.description = channelDescription

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    @Deprecated("Deprecated in Java", ReplaceWith("super.onActivityResult(requestCode, resultCode, data)", "androidx.activity.ComponentActivity"))
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        //Select Artist Cover
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {

            try {

                val uri = data!!.data!!
                contentResolver.takePersistableUriPermission(uri, FLAG_GRANT_READ_URI_PERMISSION)

                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val bitmapBytes = baos.toByteArray()

                //If image is too big
                if (bitmapBytes.size > 1048576) {
                    Toast.makeText(applicationContext, getAppString(applicationContext, R.string.ImageTooBig), Toast.LENGTH_LONG).show()
                } else {

                    val bitmapString = android.util.Base64.encodeToString(bitmapBytes, android.util.Base64.DEFAULT)

                    ViewModelProvider(activityContext)[SelectArtistCoverScreenVM::class.java].updateArtistCover(bitmapString)
                }


            } catch (_: Exception) {
            }
        }

        //Select playlist cover
        if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            try {
                val uri = data!!.data!!
                contentResolver.takePersistableUriPermission(uri, FLAG_GRANT_READ_URI_PERMISSION)

                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val bitmapBytes = baos.toByteArray()

                //If image is too big it will throw a error
                if (bitmapBytes.size > 1048576) {
                    Toast.makeText(applicationContext, getAppString(applicationContext, R.string.ImageTooBig), Toast.LENGTH_LONG).show()
                } else {

                    val activityContext = this

                    runBlocking {
                        withContext(Dispatchers.IO) {

                            val bitmapString = android.util.Base64.encodeToString(bitmapBytes, android.util.Base64.DEFAULT)

                            ViewModelProvider(activityContext)[PlaylistScreenVM::class.java].onImageReceived(bitmapString)
                        }
                    }
                }
            } catch (_: Exception) {
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}







