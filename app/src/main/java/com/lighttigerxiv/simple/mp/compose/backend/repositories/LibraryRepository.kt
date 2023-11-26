package com.lighttigerxiv.simple.mp.compose.backend.repositories

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import com.lighttigerxiv.simple.mp.compose.backend.realm.Queries
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Album
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Artist
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.backend.realm.getRealm
import com.lighttigerxiv.simple.mp.compose.backend.utils.isAtLeastAndroid10
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext


class LibraryRepository(
    private val settingsRepository: SettingsRepository
) {

    data class AlbumArt(
        val albumId: Long,
        val art: Bitmap?
    )

    private val queries = Queries(getRealm())

    private val _initialized = MutableStateFlow(false)
    val initialized = _initialized.asStateFlow()

    private val _songs = MutableStateFlow<List<Song>>(ArrayList())
    val songs = _songs.asStateFlow()

    private val _artists = MutableStateFlow<List<Artist>>(ArrayList())
    val artists = _artists.asStateFlow()

    private val _albums = MutableStateFlow<List<Album>>(ArrayList())
    val albums = _albums.asStateFlow()

    private val _smallAlbumArts = MutableStateFlow<List<AlbumArt>>(ArrayList())
    val smallAlbumArts = _smallAlbumArts.asStateFlow()

    private val _albumArts = MutableStateFlow<List<AlbumArt>>(ArrayList())
    val albumArts = _albumArts.asStateFlow()

    private val _indexingLibrary = MutableStateFlow(false)
    val indexingLibrary = _indexingLibrary.asStateFlow()

    private suspend fun loadLibrary(onFinish: suspend () -> Unit = {}) {
        withContext(Dispatchers.Main) {
            settingsRepository.settingsFlow.collect { settings ->

                val durationFilter = settings.durationFilter * 1000

                val newSongs = queries.getSongs().filter { it.duration >= durationFilter }
                val newArtists = queries.getArtists().filter { artist -> newSongs.any { song -> song.artistId == artist.id } }
                val newAlbums = queries.getAlbums().filter { album -> newSongs.any { song -> song.albumId == album.id } }

                _songs.update { newSongs }
                _artists.update { newArtists }
                _albums.update { newAlbums }

                onFinish()
            }
        }
    }

    private suspend fun loadAlbumArts(context: Context) {
        withContext(Dispatchers.Default) {

            val filteredSongs = songs.value.distinctBy { it.albumId }

            filteredSongs.forEach { song ->

                val newSmallAlbumArts = smallAlbumArts.value.toMutableList().apply {
                    add(
                        AlbumArt(
                            song.albumId,
                            getAlbumArt(context, song.id, song.albumId, smallSize = true)
                        )
                    )
                }

                val newAlbumArts = albumArts.value.toMutableList().apply {
                    add(
                        AlbumArt(
                            song.albumId,
                            getAlbumArt(context, song.id, song.albumId)
                        )
                    )
                }

                _smallAlbumArts.update { newSmallAlbumArts }
                _albumArts.update { newAlbumArts }
            }
        }
    }

    suspend fun initLibrary(context: Context) {
        loadLibrary{
            withContext(Dispatchers.Main){
                loadAlbumArts(context)
            }
        }
    }

    suspend fun indexLibrary(
        context: Context,
        onFinish: suspend () -> Unit = {}
    ) {

        if (!indexingLibrary.value) {

            _indexingLibrary.update { true }

            val songs: ArrayList<Song> = ArrayList()
            val artists: ArrayList<Artist> = ArrayList()
            val albums: ArrayList<Album> = ArrayList()

            val mediaRetriever = MediaMetadataRetriever()
            val queries = Queries(getRealm())

            val cursor = context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null)

            cursor?.let {

                while (cursor.moveToNext()) {

                    try {

                        val id = cursor.getLong(cursor.getColumn(MediaStore.Audio.Media._ID))
                        val path = cursor.getString(cursor.getColumn(MediaStore.Audio.Media.DATA))

                        mediaRetriever.setDataSource(path)

                        val name = mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: cursor.getString(cursor.getColumn(MediaStore.Audio.Media.TITLE))
                        val albumName = mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: cursor.getString(cursor.getColumn(MediaStore.Audio.Albums.ALBUM))
                        val albumId = cursor.getLong(cursor.getColumn(MediaStore.Audio.Media.ALBUM_ID))
                        val duration = mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: cursor.getString(cursor.getColumn(MediaStore.Audio.Media.DURATION))
                        val artistName = mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST) ?: cursor.getString(cursor.getColumn(MediaStore.Audio.Artists.ARTIST))
                        val artistId = cursor.getLong(cursor.getColumn(MediaStore.Audio.Media.ARTIST_ID))
                        val releaseYear = cursor.getInt(cursor.getColumn(MediaStore.Audio.Media.YEAR))
                        val genre = mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE) ?: "Unknown"


                        if (name != null && duration != null && artistId != 0L && albumId != 0L) {

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
                                album.artistId = artistId

                                albums.add(album)
                            }

                            val song = Song()
                            song.id = id
                            song.path = path
                            song.name = name
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

                albums.forEach { album -> queries.addAlbum(album.id, album.name, album.artistId) }
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

            _indexingLibrary.update { false }
            onFinish()
        }
    }

    private fun Cursor.getColumn(column: String): Int {
        return this.getColumnIndex(column)
    }

    @Suppress("DEPRECATION")
    private fun getAlbumArt(context: Context, songId: Long, albumId: Long, smallSize: Boolean = false): Bitmap? {

        var art: Bitmap? = null
        val size = if (smallSize) 200 else 400

        try {

            art = if (isAtLeastAndroid10()) {
                val uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    songId
                )

                context.contentResolver.loadThumbnail(uri, Size(size, size), null)
            } else {
                val uri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"),
                    albumId
                )

                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }

        } catch (e: Exception) {
            Log.d("AlbumArt", e.toString())
        }

        return art
    }


    fun getSmallAlbumArt(albumId: Long): Bitmap? {
        return smallAlbumArts.value.find { it.albumId == albumId }?.art
    }

    fun getLargeAlbumArt(albumId: Long): Bitmap? {
        return albumArts.value.find { it.albumId == albumId }?.art
    }

    fun getArtistName(artistId: Long): String {
        return artists.value.find { it.id == artistId }?.name ?: "n/a"
    }


    fun getArtistSongs(artistId: Long): List<Song> {
        return songs.value.filter { it.artistId == artistId }
    }

    fun getArtistAlbums(artistId: Long): List<Album> {
        return albums.value.filter { it.artistId == artistId }
    }

    fun getAlbumName(albumId: Long): String {
        return albums.value.find { it.id == albumId }?.name ?: "n/a"
    }

    fun getAlbumSongs(albumId: Long): List<Song> {
        return songs.value.filter { it.albumId == albumId }
    }
}