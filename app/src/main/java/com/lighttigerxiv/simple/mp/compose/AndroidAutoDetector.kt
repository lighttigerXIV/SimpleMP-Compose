package com.lighttigerxiv.simple.mp.compose

import android.annotation.SuppressLint
import android.content.AsyncQueryHandler
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import androidx.core.content.ContextCompat


class AndroidAutoDetector(val context: Context) {

    var onDisconnect: () -> Unit = {}

    companion object {
        const val TAG = "AutoConnectionDetector"

        // columnName for provider to query on connection status
        const val CAR_CONNECTION_STATE = "CarConnectionState"

        // auto app on your phone will send broadcast with this action when connection state changes
        const val ACTION_CAR_CONNECTION_UPDATED = "androidx.car.app.connection.action.CAR_CONNECTION_UPDATED"

        // phone is not connected to car
        const val CONNECTION_TYPE_NOT_CONNECTED = 0

        // phone is connected to Automotive OS
        const val CONNECTION_TYPE_NATIVE = 1

        // phone is connected to Android Auto
        const val CONNECTION_TYPE_PROJECTION = 2

        private const val QUERY_TOKEN = 42

        private const val CAR_CONNECTION_AUTHORITY = "androidx.car.app.connection"

        private val PROJECTION_HOST_URI = Uri.Builder().scheme("content").authority(CAR_CONNECTION_AUTHORITY).build()
    }

    private val carConnectionReceiver = CarConnectionBroadcastReceiver()
    private val carConnectionQueryHandler = CarConnectionQueryHandler(context.contentResolver)


    fun registerCarConnectionReceiver() {

        ContextCompat.registerReceiver(context, carConnectionReceiver, IntentFilter(ACTION_CAR_CONNECTION_UPDATED), ContextCompat.RECEIVER_NOT_EXPORTED)
        queryForState()
    }

    fun unRegisterCarConnectionReceiver() {
        context.unregisterReceiver(carConnectionReceiver)
    }

    private fun queryForState() {
        carConnectionQueryHandler.startQuery(
            QUERY_TOKEN,
            null,
            PROJECTION_HOST_URI,
            arrayOf(CAR_CONNECTION_STATE),
            null,
            null,
            null
        )
    }

    inner class CarConnectionBroadcastReceiver : BroadcastReceiver() {
        // query for connection state every time the receiver receives the broadcast
        override fun onReceive(context: Context?, intent: Intent?) {
            queryForState()
        }
    }

    internal class CarConnectionQueryHandler(resolver: ContentResolver?) : AsyncQueryHandler(resolver) {
        // notify new queryed connection status when query complete
        override fun onQueryComplete(token: Int, cookie: Any?, response: Cursor?) {
            if (response == null) {

                return
            }
            val carConnectionTypeColumn = response.getColumnIndex(CAR_CONNECTION_STATE)
            if (carConnectionTypeColumn < 0) {

                return
            }
            if (!response.moveToNext()) {

                return
            }
            val connectionState = response.getInt(carConnectionTypeColumn)
            if (connectionState == CONNECTION_TYPE_NOT_CONNECTED) {

            } else {

            }
        }
    }
}