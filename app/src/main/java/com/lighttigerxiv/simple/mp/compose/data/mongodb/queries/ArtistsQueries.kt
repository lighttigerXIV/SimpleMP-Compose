package com.lighttigerxiv.simple.mp.compose.data.mongodb.queries

import com.lighttigerxiv.simple.mp.compose.data.mongodb.items.Artist
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

class ArtistsQueries(
    private val realm: Realm
) {

    fun getArtist(artistID: Long): Artist?{

        return realm.query<Artist>("artistID == $0", artistID).first().find()
    }

    fun addArtist(artist: Long){

        realm.writeBlocking {

            this.copyToRealm(Artist().apply {
                artistID = artist
                image = null
                alreadyRequested = false
            })
        }
    }

    suspend fun updateArtistCover(artistID: Long, imageString: String?){

        realm.write {

            val artist = this.query<Artist>("artistID == $0", artistID).find().first()

            artist.image = imageString
        }
    }

    suspend fun updateArtistAlreadyRequested(artistID: Long){

        realm.write {

            val artist = this.query<Artist>("artistID == $0", artistID).find().first()

            artist.alreadyRequested = true
        }
    }
}