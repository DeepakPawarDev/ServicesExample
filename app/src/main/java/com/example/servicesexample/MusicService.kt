package com.example.servicesexample

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.ServiceCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class MusicService : Service() {

    private val player: Player by lazy {
        ExoPlayer.Builder(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize the player here.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val recordingUri = intent?.data
        player.setMediaItem(MediaItem.fromUri(recordingUri!!))
        player.prepare()
        player.play()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the player here.
        player.release()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}