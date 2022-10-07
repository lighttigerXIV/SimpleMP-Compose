package com.lighttigerxiv.simple.mp.compose.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.lighttigerxiv.simple.mp.compose.activities.MainActivity

class ReceiverOpenApp: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        val newIntent = Intent( context, MainActivity::class.java ).addFlags( FLAG_ACTIVITY_NEW_TASK )
        newIntent.putExtra( "restore", true )

        context.startActivity( newIntent )
    }
}