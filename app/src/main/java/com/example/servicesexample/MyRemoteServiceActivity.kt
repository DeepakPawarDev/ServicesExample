package com.example.servicesexample

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MyRemoteServiceActivity : AppCompatActivity() {
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var button5: Button
    lateinit var myService: MyRemoteService
    lateinit var serviceIntent: Intent
    private var isServiceBound = false
    lateinit var serviceConnection: ServiceConnection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_remote_service)

        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)
        button5 = findViewById(R.id.button5)


        setListeners()
    }

    fun getMyServiceIntent(): Intent {
        if (!::serviceIntent.isInitialized) {
            serviceIntent = Intent(this, MyRemoteService::class.java)
            return serviceIntent
        } else {
            return serviceIntent
        }
    }

    fun setListeners() {
        button1.setOnClickListener {
            //Start service
            startService(getMyServiceIntent())
        }

        button2.setOnClickListener {
            // Stop service
            stopService(getMyServiceIntent())
        }

        button3.setOnClickListener {
            // Bind service

            serviceConnection = object : ServiceConnection {
                override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                    /*myService = (p1 as MyService.MyBindService).getMyService()
                    isServiceBound = true
                    Log.i(Companion.TAG, "*************************** Service BOUND")*/

                }

                override fun onServiceDisconnected(p0: ComponentName?) {
                    isServiceBound = false
                    Log.i(Companion.TAG, "*************************** Service UNBOUND")

                }
            }
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        }

        button4.setOnClickListener {
            // get random number

            if (isServiceBound) {
                Log.i(Companion.TAG, "*************************** Number - ${myService.getRandomNumber()}")
            } else {
                Log.i(Companion.TAG, "************************** Service is not bound")
            }
        }
        button5.setOnClickListener {
            // Unbind service
            if (isServiceBound) {
                unbindService(serviceConnection)
                isServiceBound = false
            }
        }
    }

    companion object {
        private const val TAG = "MyRemoteServiceActivity"
    }

}
