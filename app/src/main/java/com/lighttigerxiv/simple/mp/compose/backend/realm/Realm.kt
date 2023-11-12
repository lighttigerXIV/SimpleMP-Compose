package com.lighttigerxiv.simple.mp.compose.backend.realm

import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Album
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Artist
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

fun getRealm(): Realm{
    val config = RealmConfiguration.Builder(
        schema = setOf(
            Song::class,
            Album::class,
            Artist::class
        )
    )
        .schemaVersion(10)
        .compactOnLaunch()
        .build()

    return Realm.open(config)
}