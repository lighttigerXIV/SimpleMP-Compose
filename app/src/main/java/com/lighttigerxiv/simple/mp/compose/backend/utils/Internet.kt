package com.lighttigerxiv.simple.mp.compose.backend.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.lighttigerxiv.simple.mp.compose.backend.settings.Settings

fun isNetworkAvailable(context: Context): Boolean {

    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

    if (capabilities != null) {

        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) return true
        else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return true
        else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) return true
    }
    return false
}

fun isOnWifi(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

    capabilities?.let {
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    return false
}

fun isOnMobileData(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) return true
    }
    return false
}

fun canDownloadArtistImages(settings: Settings, context: Context): Boolean {
    return settings.downloadArtistCover && (isOnWifi(context) || (settings.downloadArtistCoverWithData && isOnMobileData(context)))
}