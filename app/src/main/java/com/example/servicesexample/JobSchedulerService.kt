package com.example.servicesexample

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import java.util.Random

class JobSchedulerService : JobService() {
    private  val TAG = "MyRemoteService"
    private var mRandomNumber = 0
    private var mIsRandomGeneratorOn = false
    private val MIN = 0
    private val MAX = 100
    override fun onStartJob(p0: JobParameters?): Boolean {
        mIsRandomGeneratorOn = true
        doBackgroundWork()
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        mIsRandomGeneratorOn = false
        return true
    }
private fun doBackgroundWork(){
    Log.i(TAG,"doBackgroundWork")
   Thread( Runnable { startRandomNumberGenerator() }).start()
}

    private fun startRandomNumberGenerator() {
        while (mIsRandomGeneratorOn) {
            try {
                Thread.sleep(1000)
                if (mIsRandomGeneratorOn) {
                    mRandomNumber = Random().nextInt(MAX) + MIN
                    Log.i(
                        TAG,
                        "Thread id: " + Thread.currentThread().id + ", Random Number: " + mRandomNumber
                    )
                }
            } catch (e: InterruptedException) {
                Log.i(TAG, "Thread Interrupted")
            }
        }
    }
}


