package com.lighttigerxiv.simple.mp.compose.backend.realm.collections

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ArtistImageRequest: RealmObject {
    @PrimaryKey var artistId: Long = 0L
    var useDefault: Boolean = false
}