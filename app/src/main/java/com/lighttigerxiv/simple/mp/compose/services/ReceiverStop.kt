package com.lighttigerxiv.simple.mp.compose.services

import android.content.*

class ReceiverStop: BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent?) {

        val actionsServiceIntent = Intent( context, ActionsService::class.java )

        actionsServiceIntent.putExtra( "action", "stop" )

        context.startService( actionsServiceIntent )
    }



}