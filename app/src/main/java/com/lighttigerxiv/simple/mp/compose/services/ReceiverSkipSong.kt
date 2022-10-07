package com.lighttigerxiv.simple.mp.compose.services

import android.content.*

class ReceiverSkipSong: BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent?) {


        val actionsServiceIntent = Intent( context, ActionsService::class.java )

        actionsServiceIntent.putExtra( "action", "skip" )

        context.startService( actionsServiceIntent )
    }



}