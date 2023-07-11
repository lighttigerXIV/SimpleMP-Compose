package com.lighttigerxiv.simple.mp.compose.data.mongodb.queries

import com.lighttigerxiv.simple.mp.compose.data.data_classes.Album
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Artist
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.data.mongodb.items.CachedAlbum
import com.lighttigerxiv.simple.mp.compose.data.mongodb.items.CachedArtist
import com.lighttigerxiv.simple.mp.compose.data.mongodb.items.CachedSong
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

class CacheQueries(
    private val realm: Realm
) {

    fun getSongs(): List<CachedSong>{
        return realm.query<CachedSong>().find()
    }

    fun getArtists(): List<CachedArtist>{
        return realm.query<CachedArtist>().find()
    }

    fun getAlbums(): List<CachedAlbum>{
        return realm.query<CachedAlbum>().find()
    }

    fun addSong(song: Song){

        val cachedSong = CachedSong()
        cachedSong.id = song.id
        cachedSong.path = song.path
        cachedSong.title = song.title
        cachedSong.albumID = song.albumID
        cachedSong.duration = song.duration
        cachedSong.artistID = song.artistID
        cachedSong.year = song.year
        cachedSong.genre = song.genre
        cachedSong.modificationDate = song.modificationDate

        realm.writeBlocking {
            this.copyToRealm(cachedSong)
        }
    }

    fun addArtist(artist: Artist){

        val cachedArtist = CachedArtist()
        cachedArtist.id = artist.id
        cachedArtist.name = artist.name

        realm.writeBlocking {
            this.copyToRealm(cachedArtist)
        }
    }

    fun addAlbum(album: Album){

        val cachedAlbum = CachedAlbum()
        cachedAlbum.id = album.id
        cachedAlbum.title = album.title
        cachedAlbum.artistID = album.artistID

        realm.writeBlocking {
            this.copyToRealm(cachedAlbum)
        }
    }

    suspend fun clear(){
        realm.write {
            val songs = this.query<CachedSong>().find()
            val artists = this.query<CachedArtist>().find()
            val albums = this.query<CachedAlbum>().find()

            delete(songs)
            delete(artists)
            delete(albums)
        }
    }
}