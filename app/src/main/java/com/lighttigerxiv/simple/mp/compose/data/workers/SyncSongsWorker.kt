package com.lighttigerxiv.simple.mp.compose.data.workers

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Album
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Artist
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.data.mongodb.getMongoRealm
import com.lighttigerxiv.simple.mp.compose.data.mongodb.queries.CacheQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class SyncSongsWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    @SuppressLint("Range")
    override suspend fun doWork(): Result {

        return withContext(Dispatchers.IO){

            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationBuilder = NotificationCompat.Builder(applicationContext, "Sync")
                .setContentTitle("Syncing Songs")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.play_empty)
                .setSilent(true)

            val cachedQueries = CacheQueries(getMongoRealm())

            val songs = cachedQueries.getSongs()
            val artists = cachedQueries.getArtists()
            val albums = cachedQueries.getAlbums()
            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

            val cursor = applicationContext.contentResolver.query(uri, null, null, null, null)
                ?: return@withContext Result.failure() //No mediastore rows found
            val cachedSongIds = songs.map { it.id }.toHashSet()
            val totalSongsCount = cursor.count
            var indexedSongsCount = 0

            while (cursor.moveToNext()) {
                try {
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    if (cachedSongIds.contains(id)){ //cache hit or miss
                        indexedSongsCount++
                        continue
                    }

                    val songPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))

                    val retriever = MediaMetadataRetriever()
                    retriever.setDataSource(songPath)

                    var title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    var albumTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                    val albumID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                    var duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    var artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)
                    val artistID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID))
                    var genre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)
                    val modificationDate = File(songPath).lastModified()


                    if (title == null) {
                        title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    }

                    if (albumTitle == null) {
                        albumTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
                    }

                    if (duration == null) {
                        duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    }

                    if (artistName == null) {
                        artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST))
                    }

                    if (!artists.any { it.id == artistID }) {

                        val artist = Artist(
                            artistID,
                            artistName ?: "",
                            null
                        )
                        cachedQueries.addArtist(artist)
                    }

                    if (!albums.any { it.id == albumID }) {

                        val album = Album(
                            albumID,
                            albumTitle ?: "",
                            null,
                            artistID
                        )
                        cachedQueries.addAlbum(album)
                    }


                    if (genre == null) genre = applicationContext.getString(R.string.Undefined)
                    val year = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR))

                    if (title != null && duration != null && artistID != 0L && albumID != 0L) {

                        val song = Song(id, songPath, title, albumID, duration.toInt(), artistID, year, genre, modificationDate)
                        cachedQueries.addSong(song)

                        indexedSongsCount++

                        notificationBuilder.setProgress(totalSongsCount, indexedSongsCount, false)
                        notificationManager.notify(3, notificationBuilder.build())
                    }

                } catch (e: Exception) {
                    Log.e("Song Error", "Exception while getting song. Details -> ${e.message}")
                }
            }

            cursor.close()

            notificationManager.cancel(3)

            return@withContext Result.success()
        }
    }
}