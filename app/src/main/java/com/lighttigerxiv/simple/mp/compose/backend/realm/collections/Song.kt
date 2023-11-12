package com.lighttigerxiv.simple.mp.compose.backend.realm.collections

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Song: RealmObject {
    @PrimaryKey var id: Long = 0L
    var path: String = ""
    var name: String = ""
    var albumId: Long = 0L
    var artistId: Long = 0L
    var duration: Int = 0
    var releaseYear: Int = 0
    var genre: String = ""
    var modificationDate: Long = 0L
}