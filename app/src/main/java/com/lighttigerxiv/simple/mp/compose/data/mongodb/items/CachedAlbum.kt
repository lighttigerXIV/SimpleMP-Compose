package com.lighttigerxiv.simple.mp.compose.data.mongodb.items

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CachedAlbum: RealmObject {
    @PrimaryKey
    var id: Long = 0
    var title: String = ""
    var artistID: Long = 0
}