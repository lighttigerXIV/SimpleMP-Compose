@file:Suppress("PropertyName")

package com.lighttigerxiv.simple.mp.compose.data.mongodb.items

import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Playlist: RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var name: String = ""
    var image: String? = null
    var songs: RealmList<Long> = ArrayList<Long>().toRealmList()
}