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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.activities.setup.ActivityFirstSetup
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.functions.getSurfaceColor
import com.lighttigerxiv.simple.mp.compose.screens.main.artist.artist_select_cover.SelectArtistCoverScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.main.MainScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.main.MainScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.PlaylistScreenVM
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM
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


        val mainVM = ViewModelProvider(this)[MainVM::class.java]
        val mainScreenVM = ViewModelProvider(this)[MainScreenVM::class.java]
        val settingsVM = ViewModelProvider(this)[SettingsVM::class.java]


        setContent {
            ComposeSimpleMPTheme(
                useDarkTheme = isSystemInDarkTheme(),
                themeMode = settingsVM.themeModeSetting.collectAsState().value,
                themeAccent = settingsVM.themeAccentSetting.collectAsState().value,
                content = {

                    mainVM.updateSurfaceColor(getSurfaceColor(settingsVM = settingsVM))

                    rememberSystemUiController().apply {
                        setStatusBarColor(mainVM.surfaceColor.collectAsState().value)
                        setNavigationBarColor(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant)
                    }

                    MainScreen(
                        mainVM = mainVM,
                        mainScreenVM = mainScreenVM,
                        activityContext = activityContext,
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







