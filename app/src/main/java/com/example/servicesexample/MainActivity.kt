package com.example.servicesexample

import android.app.job.JobInfo
import android.app.job.JobScheduler
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

class MainActivity : AppCompatActivity() {
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var button5: Button
    private lateinit var button6: Button
    private lateinit var button7: Button


    lateinit var myService: MyService
    lateinit var serviceIntent: Intent
    private var isServiceBound = false
    lateinit var serviceConnection: ServiceConnection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)
        button5 = findViewById(R.id.button5)
        button6 = findViewById(R.id.button6)
        button7 = findViewById(R.id.button7)


        setListeners()
    }

    fun getMyServiceIntent(): Intent {
        if (!::serviceIntent.isInitialized) {
            serviceIntent= Intent(this, MyService::class.java)
            return serviceIntent
        }else{
            return serviceIntent
        }
    }

    fun setListeners() {

        button6.setOnClickListener {
            //Start Remote Activity
            startActivity(Intent(this, MyRemoteServiceActivity::class.java))

        }

        button7.setOnClickListener {
            //Start Job Scheduler or Schedule Job

            scheduleJob()
        }

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
                    myService = (p1 as MyService.MyBindService).getMyService()
                    isServiceBound = true
                    Log.i(TAG, "*************************** Service BOUND")

                }

                override fun onServiceDisconnected(p0: ComponentName?) {
                    isServiceBound = false
                    Log.i(TAG, "*************************** Service UNBOUND")

                }
            }
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        }

        button4.setOnClickListener {
            // get random number

            if (isServiceBound) {
                Log.i(TAG, "*************************** Number - ${myService.getRandomNumber()}")
            } else {
                Log.i(TAG, "************************** Service is not bound")
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

    private fun scheduleJob() {
      val component =  ComponentName(this, JobSchedulerService::class.java)
        val jobInfo = JobInfo.Builder(1, component)
            .setRequiresCharging(true)
            .build()

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

       if ( jobScheduler.schedule(jobInfo) == JobScheduler.RESULT_SUCCESS){
           Log.i(TAG, "*************************** Job Scheduled")
       }else{
           Log.i(TAG, "*************************** Job Not Scheduled")
       }



    }

    private val TAG = "MainActivity"
}