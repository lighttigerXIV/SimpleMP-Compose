@file:Suppress("PropertyName")

package com.lighttigerxiv.simple.mp.compose.data.mongodb.items

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey


class Artist: RealmObject{

    @PrimaryKey
    var artistID: Long = 0
    var image: String? = null
    var alreadyRequested : Boolean = false
}