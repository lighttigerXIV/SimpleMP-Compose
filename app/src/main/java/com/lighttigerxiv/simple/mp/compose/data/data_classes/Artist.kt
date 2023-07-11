package com.lighttigerxiv.simple.mp.compose.data.data_classes

import android.graphics.Bitmap

data class Artist(
    val id: Long,
    val name: String,
    val cover: Bitmap?
)
