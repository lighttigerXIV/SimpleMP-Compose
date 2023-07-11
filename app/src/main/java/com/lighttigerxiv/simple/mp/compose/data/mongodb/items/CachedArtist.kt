package com.lighttigerxiv.simple.mp.compose.data.mongodb.items

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CachedArtist : RealmObject {
    @PrimaryKey
    var id: Long = 0
    var name: String = ""
}