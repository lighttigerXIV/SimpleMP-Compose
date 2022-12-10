package com.lighttigerxiv.simple.mp.compose

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size

class GetSongs {

    companion object {

        @SuppressLint("Range")
        fun getSongsList(context: Context, sortType: String): ArrayList<Song> {

            try {

                val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                val songsList = ArrayList<Song>()


                if (cursor != null && cursor.count > 0) {
                    if (cursor.moveToNext()) {
                        do {

                            val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                            val songPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                            val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                            val albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
                            val albumID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                            val duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                            val artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                            val artistID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID))


                            val genreID = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                                cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.GENRE_ID))
                            else
                                cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Genres._ID))


                            var genre = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.GENRE))
                            else
                                null


                            if (genre == null) genre = context.getString(R.string.Undefined)
                            val year = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR))


                            val song = Song(id, songPath, title, albumName, albumID, duration, artistName, artistID, year, genreID, genre)

                            val filterDuration = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).getString("FilterAudio", "60")!!.toInt() * 1000
                            if (duration > filterDuration) songsList.add(song)
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