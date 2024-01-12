package com.lighttigerxiv.simple.mp.compose.backend.utils

import android.graphics.Bitmap

fun Bitmap.compressed(): Bitmap {

    val originalWidth = this.width
    val originalHeight = this.height

    var newWidth = 0f
    var newHeight = 0f

    val ratio = originalWidth.toFloat() / originalHeight.toFloat()
    if (originalWidth > originalHeight) {
        newHeight = 512f
        newWidth = newHeight * ratio
    } else {
        newWidth = 512f
        newHeight = newWidth / ratio
    }

    return Bitmap.createScaledBitmap(this, newWidth.toInt(), newHeight.toInt(), true)
}