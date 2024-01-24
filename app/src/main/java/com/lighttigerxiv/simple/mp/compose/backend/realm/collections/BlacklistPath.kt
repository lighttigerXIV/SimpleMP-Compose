package com.lighttigerxiv.simple.mp.compose.backend.realm.collections

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class BlacklistPath: RealmObject {
    @PrimaryKey var path: String = ""
}