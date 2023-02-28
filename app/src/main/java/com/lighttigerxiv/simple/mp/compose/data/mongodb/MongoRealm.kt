package com.lighttigerxiv.simple.mp.compose.data.mongodb

import com.lighttigerxiv.simple.mp.compose.data.mongodb.items.Artist
import com.lighttigerxiv.simple.mp.compose.data.mongodb.items.Playlist
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration


fun getMongoRealm(): Realm{
    val config = RealmConfiguration.Builder(
        schema = setOf(
            Playlist::class,
            Artist::class
        )
    )
        .schemaVersion(7)
        .compactOnLaunch()
        .build()

    return Realm.open(config)
}

