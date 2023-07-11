package com.lighttigerxiv.simple.mp.compose.data.mongodb.items

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CachedSong : RealmObject {
    @PrimaryKey
    var id: Long = 0
    var path: String = ""
    var title: String = ""
    var albumID: Long = 0
    var duration: Int = 0
    var artistID: Long = 0
    var year: Int = 0
    var genre: String = ""
    var modificationDate: Long = 0
}