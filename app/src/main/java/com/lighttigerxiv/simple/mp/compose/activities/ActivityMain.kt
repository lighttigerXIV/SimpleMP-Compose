package com.lighttigerxiv.simple.mp.compose.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lighttigerxiv.simple.mp.compose.ActivityFirstSetup
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.MainVM
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.SettingsVM
import com.lighttigerxiv.simple.mp.compose.getSurfaceColor
import com.lighttigerxiv.simple.mp.compose.navigation.screens.*
import com.lighttigerxiv.simple.mp.compose.navigation.screens.main.*
import com.lighttigerxiv.simple.mp.compose.screens.main.main.MainScreen
import com.lighttigerxiv.simple.mp.compose.ui.theme.ComposeSimpleMPTheme
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!getSharedPreferences(packageName, MODE_PRIVATE).getBoolean("setupCompleted", false)) {

            startActivity(
                Intent(applicationContext, ActivityFirstSetup::class.java)
            )

            finish()
        }

        createNotificationChannel()

        val activityContext = this
        val mainVM = ViewModelProvider(this)[MainVM::class.java]
        val settingsVM = ViewModelProvider(this)[SettingsVM::class.java]

        setContent {
            ComposeSimpleMPTheme(
                useDarkTheme = isSystemInDarkTheme(),
                themeMode = mainVM.themeModeSetting.collectAsState().value,
                themeAccent = mainVM.themeAccentSetting.collectAsState().value,
                content = {

                    val surfaceColor = mainVM.surfaceColor.collectAsState().value

                    mainVM.updateSurfaceColor(getSurfaceColor(settingsVM = settingsVM))

                    rememberSystemUiController().apply {
                        setStatusBarColor(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant)
                        setNavigationBarColor(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant)
                    }

                    MainScreen(
                        mainVM = mainVM,
                        settingsVM = settingsVM,
                        activityContext = activityContext
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

        /*
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {

            try {

                val uri = data!!.data!!
                contentResolver.takePersistableUriPermission(uri, FLAG_GRANT_READ_URI_PERMISSION)

                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val bitmapBytes = baos.toByteArray()

                if (bitmapBytes.size > 1048576) {
                    Toast.makeText(applicationContext, getAppString(applicationContext, R.string.ImageTooBig), Toast.LENGTH_LONG).show()
                } else {
                    val bitmapString = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
                    mainVM.onPlaylistImageSelected(bitmapString)
                }


            } catch (_: Exception) {}
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 2){
            try {
                val uri = data!!.data!!
                contentResolver.takePersistableUriPermission(uri, FLAG_GRANT_READ_URI_PERMISSION)

                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val bitmapBytes = baos.toByteArray()

                if (bitmapBytes.size > 1048576) {
                    Toast.makeText(applicationContext, getAppString(applicationContext, R.string.ImageTooBig), Toast.LENGTH_LONG).show()
                } else {
                    val bitmapString = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
                    mainVM.onArtistImageSelected(bitmapString)
                }
            } catch (_: Exception) {}
        }

         */

        super.onActivityResult(requestCode, resultCode, data)
    }
}







