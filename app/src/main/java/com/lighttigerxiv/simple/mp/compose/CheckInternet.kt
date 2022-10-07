package com.lighttigerxiv.simple.mp.compose

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class CheckInternet {

    companion object{

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
    }
}