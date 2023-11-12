package com.lighttigerxiv.simple.mp.compose.backend.utils

import android.os.Build

fun isAtLeastAndroid12(): Boolean{
    return Build.VERSION.SDK_INT >= 31
}

fun isAtLeastAndroid13(): Boolean{
    return Build.VERSION.SDK_INT >= 33
}