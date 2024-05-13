package com.example.servicesexample

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.Random


class MyService : Service() {


    private var mRandomNumber = 0
    private var mIsRandomGeneratorOn = false

    private val MIN = 0
    private val MAX = 100
    val GET_COUNT = 0
    private val iBindService: IBinder = MyBindService()
    override fun onBind(p0: Intent?): IBinder? {
        return iBindService
    }
inner class MyBindService : Binder() {
    fun getMyService(): MyService {
        return this@MyService
    }

}

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.i(
            TAG,
            "In onStartCommend, thread id: " + Thread.currentThread().id
        )
        mIsRandomGeneratorOn = true
        Thread { startRandomNumberGenerator() }.start()
        return START_STICKY
    }

    private fun startRandomNumberGenerator() {
        while (mIsRandomGeneratorOn) {
            try {
                Thread.sleep(1000)
                if (mIsRandomGeneratorOn) {
                    mRandomNumber = Random().nextInt(MAX) + MIN
                    Log.i(
                        Companion.TAG,
                        "Thread id: " + Thread.currentThread().id + ", Random Number: " + mRandomNumber
                    )
                }
            } catch (e: InterruptedException) {
                Log.i(Companion.TAG, "Thread Interrupted")
            }
        }
    }

    private fun stopRandomNumberGenerator() {
        mIsRandomGeneratorOn = false
    }
    override fun onDestroy() {
        super.onDestroy()
        stopRandomNumberGenerator()
        Log.i(TAG, "Service Destroyed")
    }
    companion object {
        private const val TAG = "MyService"
        private const val CHANNEL_ID = "media_playback_channel"

    }

fun getRandomNumber():Int{
    return mRandomNumber
}
}