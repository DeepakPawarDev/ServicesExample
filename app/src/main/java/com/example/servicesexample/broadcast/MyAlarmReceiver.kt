package com.example.servicesexample.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.servicesexample.MusicService

class MyAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {
        // Start your service here
        val serviceIntent = Intent(context, MusicService::class.java)
        context!!.startService(serviceIntent)
    }
}