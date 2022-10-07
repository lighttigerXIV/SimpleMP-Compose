package com.lighttigerxiv.simple.mp.compose.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class HeadphoneJackReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val actionsServiceIntent = Intent( context, ActionsService::class.java )

        if(intent?.action == Intent.ACTION_HEADSET_PLUG){

            when(intent.getIntExtra("state", -1)){

                0-> actionsServiceIntent.putExtra( "action", "pause" )
            }
        }

        context?.startService( actionsServiceIntent )
    }
}