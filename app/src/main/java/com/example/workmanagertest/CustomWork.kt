package com.example.workmanagertest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class CustomWork(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val channelId = "com.example.workmanagertest.channel1"

    override fun doWork(): Result {
        return try {
            displayNotification()
            Log.i("kkkCat", "Work Done!")
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun displayNotification() {
        val notificationId = 78

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification: Notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("demo")
            .setContentText("Periodic Work Demo!!")
            .setSmallIcon(R.drawable.ic_baseline_stars)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Demo Notification", NotificationManager.IMPORTANCE_HIGH).apply {
                    description = "A Notification"
                }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notification)
    }
}
