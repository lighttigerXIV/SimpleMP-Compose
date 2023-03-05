package com.lighttigerxiv.simple.mp.compose.functions

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaMetadataRetriever
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.data.data_classes.SongArt
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM


//************************************************
// Song Related
//************************************************


@SuppressLint("Range")
fun getSongs(context: Context, sortType: String): List<Song> {

    val songs = ArrayList<Song>()
    val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    val cursor = context.contentResolver.query(uri, null, null, null, null)

    if (cursor != null) {
        while (cursor.moveToNext()) {

            try {

                val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                val songPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))

                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(songPath)

                var title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                var albumName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                val albumID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                var duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                var artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)
                val artistID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID))
                var genre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)


                if(title == null){
                    title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                }

                if(albumName == null){
                    albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
                }

                if(duration == null){
                    duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                }

                if(artistName == null){
                    artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST))
                }


                if (genre == null) genre = context.getString(R.string.Undefined)
                val year = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR))

                if (title != null && albumName != null && artistName != null && duration != null) {

                    val song = Song(id, songPath, title, albumName, albumID, duration.toInt(), artistName, artistID, year, genre)
                    val filterDuration = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).getString("FilterAudio", "60")!!.toInt() * 1000
                    if (duration.toInt() > filterDuration) songs.add(song)

                }

            } catch (e: Exception) {
                Log.e("Song Error", "Exception while getting song. Details -> ${e.message}")
            }
        }
    }

    cursor?.close()

    when (sortType) {
        "Recent" -> songs.reverse()
        "Ascendent" -> songs.sortBy { it.title }
        "Descendent" -> songs.sortByDescending { it.title }
    }

    return songs
}

fun getAllAlbumsImages(context: Context, compressed: Boolean = false): ArrayList<SongArt> {

    val songsList = getSongs(context, sortType = "Recent")
    val songsImagesList = ArrayList<SongArt>()

    songsList.forEach { song ->

        when {

            compressed -> {

                val uncompressedAlbumArt = getSongAlbumArt(context, song.id, song.albumID)
                val compressedAlbumArt = uncompressedAlbumArt?.let { Bitmap.createScaledBitmap(it, uncompressedAlbumArt.width / 3, uncompressedAlbumArt.height / 3, false) }

                songsImagesList.add(
                    SongArt(
                        albumID = song.albumID,
                        albumArt = compressedAlbumArt
                    )
                )
            }
            else -> {

                songsImagesList.add(
                    SongArt(
                        albumID = song.albumID,
                        albumArt = getSongAlbumArt(context, song.id, song.albumID)
                    )
                )
            }
        }
    }

    return songsImagesList
}

@Suppress("DEPRECATION")
fun getSongAlbumArt(context: Context, songID: Long, albumID: Long): Bitmap? {

    var albumArt: Bitmap? = null

    try {

        albumArt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val songUri = ContentUris.withAppendedId(uri, songID)

            context.contentResolver.loadThumbnail(songUri, Size(600, 600), null)
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


fun isOnMobileData(context: Context): Boolean{
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

    if(capabilities != null){
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) return true
    }
    return false
}


//************************************************
// Others
//************************************************


fun getBitmapFromVector(context: Context, drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)
    val bitmap = Bitmap.createBitmap(
        500,
        500, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable?.setBounds(0, 0, canvas.width, canvas.height)
    drawable?.draw(canvas)
    return bitmap
}

fun getAppString(context: Context, id: Int): String{
    return context.getString(id)
}

@Composable
fun getSurfaceColor(settingsVM: SettingsVM): Color{

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