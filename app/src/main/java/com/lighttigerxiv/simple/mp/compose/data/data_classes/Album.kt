package com.lighttigerxiv.simple.mp.compose.data.data_classes

import android.graphics.Bitmap

data class Album(
    val id: Long,
    val title: String,
    val art: Bitmap?,
    val artistID: Long
)
