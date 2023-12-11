package com.lighttigerxiv.simple.mp.compose.backend.realm

import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Album
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Artist
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Playlist
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import org.mongodb.kbson.ObjectId

class Queries(private val realm: Realm) {

    suspend fun addSong(
        id: Long,
        path: String,
        name: String,
        albumId: Long,
        artistId: Long,
        duration: Int,
        releaseYear: Int,
        genre: String,
        modificationDate: Long
    ) {

        val song = Song()
        song.id = id
        song.path = path
        song.name = name
        song.albumId = albumId
        song.artistId = artistId
        song.duration = duration
        song.releaseYear = releaseYear
        song.genre = genre
        song.modificationDate = modificationDate

        realm.write {
            copyToRealm(song)
        }
    }

    fun getSongs(): List<Song> {
        return realm.query<Song>().find()
    }

    suspend fun addArtist(
        id: Long,
        name: String
    ) {

        val artist = Artist()
        artist.id = id
        artist.name = name

        realm.write {
            copyToRealm(artist)
        }
    }

    fun getArtists(): List<Artist> {
        return realm.query<Artist>().find()
    }

    suspend fun addAlbum(
        id: Long,
        name: String,
        artistId: Long
    ) {

        val album = Album()
        album.id = id
        album.name = name
        album.artistId = artistId

        realm.write {
            copyToRealm(album)
        }
    }

    fun getAlbums(): List<Album> {
        return realm.query<Album>().find()
    }

    fun getUserPlaylists(): List<Playlist> {
        return realm.query<Playlist>().find()
    }

    suspend fun createPlaylist(name: String) {
        val playlist = Playlist()
        playlist.name = name

        realm.write {
            copyToRealm(playlist)
        }
    }

    suspend fun addSongToPlaylist(playlistId: ObjectId, songId: Long) {
        realm.write {
            val playlist = this.query<Playlist>("_id == $0", playlistId).first().find()

            playlist?.let {
                if(!playlist.songs.contains(songId)){
                    playlist.songs.add(songId)
                }
            }
        }
    }

    suspend fun updatePlaylistSongs(playlistId: ObjectId, songsIds: List<Long>) {
        realm.write {
            val playlist = this.query<Playlist>("_id == $0", playlistId).first().find()
            playlist?.songs = songsIds.toRealmList()
        }
    }

    suspend fun updatePlaylistName(playlistId: ObjectId, name: String) {
        realm.write {
            val playlist = this.query<Playlist>("_id == $0", playlistId).first().find()
            playlist?.name = name
        }
    }

    suspend fun clearCache() {
        realm.write {
            val songs = query<Song>().find()
            val artists = query<Artist>().find()
            val albums = query<Album>().find()

            delete(songs)
            delete(artists)
            delete(albums)
        }
    }

    suspend fun deletePlaylist(playlistId: ObjectId) {
        realm.write {
            val playlist = this.query<Playlist>("_id == $0", playlistId).find()
            delete(playlist)
        }
    }
}