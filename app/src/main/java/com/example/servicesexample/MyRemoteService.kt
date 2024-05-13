package com.example.servicesexample

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.Random


class MyRemoteService : Service() {

    private  val TAG = "MyRemoteService"
    private var mRandomNumber = 0
    private var mIsRandomGeneratorOn = false

    private val MIN = 0
    private val MAX = 100

    private val iBindService: IBinder = MyBindService()
    private val messanger: Messenger = Messenger(MyRandomNumberGenerator())
    override fun onBind(p0: Intent?): IBinder? {

        return messanger.binder
    }
inner class MyBindService : Binder() {
    fun getMyService(): MyRemoteService {
        return this@MyRemoteService
    }

}
        override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Service Created")
    }


private inner class MyRandomNumberGenerator : Handler() {
    override fun handleMessage(msg: Message) {
        if (msg.what == GET_COUNT) {
            val randomNumber = getRandomNumber()
            val message = Message.obtain()
            message.what = GET_COUNT
            message.arg1 = randomNumber
           message.replyTo.send(message)
        }
        super.handleMessage(msg)
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
        private const val TAG = "MyRemoteService"
        private const val CHANNEL_ID = "media_playback_channel"
        val GET_COUNT = 0
    }

fun getRandomNumber():Int{
    return mRandomNumber
}
}