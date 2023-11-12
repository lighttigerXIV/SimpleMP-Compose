package com.lighttigerxiv.simple.mp.compose.backend.library

import android.content.Context
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import com.lighttigerxiv.simple.mp.compose.backend.realm.Queries
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Album
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Artist
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.backend.realm.getRealm

val MEDIA_URI: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

suspend fun indexSongs(
    context: Context,
    onFinish: () -> Unit
) {

    val songs: ArrayList<Song> = ArrayList()
    val artists: ArrayList<Artist> = ArrayList()
    val albums: ArrayList<Album> = ArrayList()

    val mediaRetriever = MediaMetadataRetriever()
    val queries = Queries(getRealm())

    val cursor = context.contentResolver.query(MEDIA_URI, null, null, null, null)

    cursor?.let {

        while (cursor.moveToNext()) {

            try {

                val id = cursor.getLong(cursor.getColumn(MediaStore.Audio.Media._ID))
                val path = cursor.getString(cursor.getColumn(MediaStore.Audio.Media.DATA))

                mediaRetriever.setDataSource(path)

                val title = mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: cursor.getString(cursor.getColumn(MediaStore.Audio.Media.TITLE))
                val albumName = mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: cursor.getString(cursor.getColumn(MediaStore.Audio.Albums.ALBUM))
                val albumId = cursor.getLong(cursor.getColumn(MediaStore.Audio.Media.ALBUM_ID))
                val duration = mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: cursor.getString(cursor.getColumn(MediaStore.Audio.Media.DURATION))
                val artistName = mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST) ?: cursor.getString(cursor.getColumn(MediaStore.Audio.Artists.ARTIST))
                val artistId = cursor.getLong(cursor.getColumn(MediaStore.Audio.Media.ARTIST_ID))
                val releaseYear = cursor.getInt(cursor.getColumn(MediaStore.Audio.Media.YEAR))
                val genre = mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE) ?: "Unknown"


                if (title != null && duration != null && artistId != 0L && albumId != 0L) {

                    if (!artists.any { it.id == artistId }) {

                        val artist = Artist()
                        artist.id = artistId
                        artist.name = artistName

                        artists.add(artist)
                    }

                    if (!albums.any { it.id == albumId }) {

                        val album = Album()
                        album.id = albumId
                        album.name = albumName
                    }

                    val song = Song()
                    song.id = id
                    song.path = path
                    song.albumId = albumId
                    song.artistId = artistId
                    song.duration = duration.toInt()
                    song.releaseYear = releaseYear
                    song.genre = genre
                    song.modificationDate

                    songs.add(song)
                }
            } catch (_: Exception) {
            }
        }

        queries.clearCache()

        albums.forEach { album -> queries.addAlbum(album.id, album.name) }
        artists.forEach { artist -> queries.addArtist(artist.id, artist.name) }
        songs.forEach { song ->
            queries.addSong(
                song.id,
                song.path,
                song.name,
                song.albumId,
                song.artistId,
                song.duration,
                song.releaseYear,
                song.genre,
                song.modificationDate
            )
        }

        cursor.close()
    }

    onFinish()
}

fun Cursor.getColumn(column: String): Int {
    return this.getColumnIndex(column)
}