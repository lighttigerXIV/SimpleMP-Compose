package com.lighttigerxiv.simple.mp.compose.backend.repositories

import com.lighttigerxiv.simple.mp.compose.backend.realm.Queries
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Playlist
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.backend.realm.getRealm
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.mongodb.kbson.ObjectId

class PlaylistsRepository{

    private val _genrePlaylists = MutableStateFlow<List<Playlist>>(ArrayList())
    val genrePlaylists = _genrePlaylists.asStateFlow()

    private val _userPlaylists = MutableStateFlow<List<Playlist>>(ArrayList())
    val userPlaylists = _userPlaylists.asStateFlow()

    private var songs: List<Song> = ArrayList()

    fun loadPlaylists(newSongs: List<Song>) {

        songs = newSongs

        val genres = songs.map { it.genre }.distinct()
        _genrePlaylists.update {
            genres.map { genre ->
                val playlist = Playlist()
                playlist.name = genre
                playlist.songs = songs.filter { song -> song.genre == genre }.map { it.id }.toRealmList()
                playlist
            }
        }

        _userPlaylists.update { Queries(getRealm()).getUserPlaylists() }
    }

    fun getGenrePlaylist(playlistId: ObjectId): Playlist {
        return genrePlaylists.value.first { it._id == playlistId }
    }

    fun getUserPlaylist(playlistId: ObjectId): Playlist? {
        return userPlaylists.value.find { it._id == playlistId }
    }

    fun getPlaylistSongs(songsIds: List<Long>): List<Song> {
        val playlistSongs = ArrayList<Song>()

        songs.forEach { song ->
            if (songsIds.contains(song.id)) {
                playlistSongs.add(song)
            }
        }

        return playlistSongs
    }
}