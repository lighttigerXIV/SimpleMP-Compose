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
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.data.variables.Settings
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM
import java.text.Normalizer


@Suppress("DEPRECATION")
fun getSongAlbumArt(context: Context, songID: Long, albumID: Long): Bitmap? {

    var albumArt: Bitmap? = null

    try {

        albumArt = if (isAtLeastAndroid10()) {

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

    val colorScheme = settingsVM.colorSchemeSetting.collectAsState().value
    val darkMode = settingsVM.darkModeSetting.collectAsState().value

    val lightColorScheme = settingsVM.lightColorSchemeSetting.collectAsState().value
    val useDarkScheme = colorScheme == Settings.Values.ColorScheme.DARK
    val useSystemScheme = colorScheme == Settings.Values.ColorScheme.SYSTEM
    val useOledSurface = darkMode == Settings.Values.DarkMode.OLED

    val useOledUnderAndroid10 = !isAtLeastAndroid10()
            && useOledSurface
            && (!lightColorScheme.startsWith("Latte")
            && lightColorScheme != Settings.Values.ColorSchemes.BLUE
            && lightColorScheme != Settings.Values.ColorSchemes.RED
            && lightColorScheme != Settings.Values.ColorSchemes.PURPLE
            && lightColorScheme != Settings.Values.ColorSchemes.ORANGE
            && lightColorScheme != Settings.Values.ColorSchemes.YELLOW
            && lightColorScheme != Settings.Values.ColorSchemes.GREEN
            && lightColorScheme != Settings.Values.ColorSchemes.PINK)

    return if (useSystemScheme && isSystemInDarkTheme() && useOledSurface)
        Color.Black
    else if (useOledUnderAndroid10)
        Color.Black
    else if (useDarkScheme && useOledSurface)
        Color.Black
    else
        MaterialTheme.colorScheme.surface
}

fun getBitmapFromSVG(context: Context, resourceID: Int): Bitmap {

    return AppCompatResources.getDrawable(context, resourceID)!!.toBitmap()
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

fun isAtLeastAndroid10(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}

fun isAtLeastAndroid12(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}