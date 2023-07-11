package com.lighttigerxiv.simple.mp.compose.functions

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM
import java.text.Normalizer


//************************************************
// Song Related
//************************************************



@Suppress("DEPRECATION")
fun getSongAlbumArt(context: Context, songID: Long, albumID: Long): Bitmap? {

    var albumArt: Bitmap? = null

    try {

        albumArt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val songUri = ContentUris.withAppendedId(uri, songID)

            context.contentResolver.loadThumbnail(songUri, Size(400, 400), null)
        } else {

            val sArtWorkUri = Uri.parse("content://media/external/audio/albumart")
            val albumArtUri = ContentUris.withAppendedId(sArtWorkUri, albumID)

            MediaStore.Images.Media.getBitmap(context.contentResolver, albumArtUri)
        }

    } catch (ignore: Exception) {
    }

    return albumArt
}



//************************************************
// Network Related
//************************************************


fun isNetworkAvailable(context: Context): Boolean {

    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

    if (capabilities != null) {

        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) return true
        else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return true
        else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) return true
    }
    return false
}


fun isOnMobileData(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) return true
    }
    return false
}


//************************************************
// Others
//************************************************


fun getImage(context: Context, drawableId: Int, imageSize: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)
    val bitmap = Bitmap.createBitmap(
        imageSize,
        imageSize, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable?.setBounds(0, 0, canvas.width, canvas.height)
    drawable?.draw(canvas)
    return bitmap
}

fun getAppString(context: Context, id: Int): String {
    return context.getString(id)
}

fun CharSequence.unaccent(): String {
    val regex = "\\p{InCombiningDiacriticalMarks}+".toRegex()

    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return regex.replace(temp, "")
}

@Composable
fun getSurfaceColor(settingsVM: SettingsVM): Color {

    val themeMode = settingsVM.themeModeSetting.collectAsState().value
    val themeAccent = settingsVM.themeAccentSetting.collectAsState().value
    val darkMode = settingsVM.darkModeSetting.collectAsState().value

    return if (themeMode == "Dark" && darkMode == "Oled") {
        Color.Black
    } else if (themeMode == "Light" && themeAccent == "Blue") {
        Color(0xFFFEFBFF)
    } else if (themeMode == "Light" && themeAccent == "Red") {
        Color(0xFFFFFBFF)
    } else if (themeMode == "Light" && themeAccent == "Purple") {
        Color(0xFFFFFBFF)
    } else if (themeMode == "Light" && themeAccent == "Yellow") {
        Color(0xFFFFFBFF)
    } else if (themeMode == "Light" && themeAccent == "Orange") {
        Color(0xFFFFFBFF)
    } else if (themeMode == "Light" && themeAccent == "Green") {
        Color(0xFFFDFDF5)
    } else if (themeMode == "Light" && themeAccent == "Pink") {
        Color(0xFFFFFBFF)
    } else if (darkMode == "Oled" && themeMode == "Light" && isSystemInDarkTheme()) {
        Color(0xFFFFFBFF)
    } else if (darkMode == "Oled" && themeAccent.startsWith("Macchiato")) {
        Color.Black
    } else if (darkMode == "Oled" && themeAccent.startsWith("Frappe")) {
        Color.Black
    } else if (darkMode == "Oled" && themeAccent.startsWith("Mocha")) {
        Color.Black
    } else if (darkMode == "Oled" && isSystemInDarkTheme()) {
        Color.Black
    } else {
        MaterialTheme.colorScheme.surface
    }
}

fun openScreen(navController: NavHostController, route: String) {

    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}