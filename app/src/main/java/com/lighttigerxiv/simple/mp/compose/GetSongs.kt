package com.lighttigerxiv.simple.mp.compose

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.*
import android.media.MediaMetadata
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import com.lighttigerxiv.simple.mp.compose.data.AppDatabase
import com.lighttigerxiv.simple.mp.compose.data.ArtistsDao

class GetSongs {


    companion object {

        @SuppressLint("Range")
        fun getSongsList(context: Context, sortType: String): ArrayList<Song> {

            try {

                val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                val songsList = ArrayList<Song>()


                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        do {

                            try {
                                val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                                val songPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))

                                val retriever = MediaMetadataRetriever()
                                retriever.setDataSource(songPath)

                                val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                                val albumName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                                val albumID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                                val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                                val artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)
                                val artistID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID))
                                var genre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)


                                if (genre == null) genre = context.getString(R.string.Undefined)
                                val year = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR))

                                if( title != null && albumName != null && artistName != null && duration != null){
                                    val song = Song(id, songPath, title, albumName, albumID, duration.toInt(), artistName, artistID, year, genre)
                                    val filterDuration = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).getString("FilterAudio", "60")!!.toInt() * 1000
                                    if (duration.toInt() > filterDuration) songsList.add(song)
                                }

                            } catch (exception: Exception){
                                println("Exception -> $exception")
                            }

                        } while (cursor.moveToNext())
                    }
                    cursor.close()
                }



                when (sortType) {

                    "Recent" -> songsList.reverse()
                    "Ascendent" -> songsList.sortBy { it.title }
                    "Descendent" -> songsList.sortByDescending { it.title }
                }



                return songsList
            } catch (exc: Exception) {
                println("Exception-> $exc")
            }

            return ArrayList()
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

            } catch (ignore: Exception) {}

            return albumArt
        }

        fun getAllAlbumsImages(context: Context, compressed: Boolean = false): ArrayList<SongArt> {

            val songsList = getSongsList(context, sortType = "Recent")
            val songsImagesList = ArrayList<SongArt>()

            songsList.forEach { song ->

                when{

                    compressed->{

                        val uncompressedAlbumArt = getSongAlbumArt(context, song.id, song.albumID)
                        val compressedAlbumArt = uncompressedAlbumArt?.let { Bitmap.createScaledBitmap(it, uncompressedAlbumArt.width / 3, uncompressedAlbumArt.height / 3, false) }

                        songsImagesList.add(
                            SongArt(
                                albumID = song.albumID,
                                albumArt = compressedAlbumArt
                            )
                        )
                    }
                    else->{

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
    }
}