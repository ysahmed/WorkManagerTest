package com.example.workmanagertest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import java.util.UUID
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var startButton: Button
    private lateinit var cancelButton: Button
    private lateinit var timeInput: EditText
    private lateinit var statusText: TextView
    private val workName = CustomWork::class.java.simpleName

    private val channelId = "com.example.workmanagertest.channel1"
    private val channelName = "Demo Channel"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById(R.id.btn_start)
        cancelButton = findViewById(R.id.btn_cancel)
        timeInput = findViewById(R.id.et_time)
        statusText = findViewById(R.id.tv_status)

        //cancelButton.isEnabled = false

        startButton.setOnClickListener {
            //startWork(15)
            startPeriodicWork()
            Log.i("kkkCat", "Click -> Start")
            //cancelButton.isEnabled = true
        }

        cancelButton.setOnClickListener {
            WorkManager.getInstance(applicationContext).cancelUniqueWork(workName)
            displayNotification()
        }
    }


    private fun startWork(interval: Long): UUID {
        val workRequest = OneTimeWorkRequest.Builder(CustomWork::class.java)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniqueWork(
                workName,
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                workRequest
            )

        return workRequest.id
    }

    private fun startPeriodicWork(){
        val periodicWorkRequest = PeriodicWorkRequest.Builder(CustomWork::class.java,20L, TimeUnit.MINUTES)
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        //val workRequest = OneTimeWorkRequest.Builder(CustomWork::class.java)

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            workName,
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
    }


    private fun displayNotification() {
        val notificationId = 78
        val notification = NotificationCompat.Builder(application.applicationContext, channelId)
            .setContentTitle("demo")
            .setContentText("Periodic Work Demo from activity")
            .setSmallIcon(R.drawable.ic_baseline_stars)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(application.applicationContext).notify(notificationId, notification)
    }
}