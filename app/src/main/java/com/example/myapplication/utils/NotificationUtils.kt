package com.example.myapplication.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.myapplication.MainActivity
import com.example.myapplication.R

private const val NOTIFICATION_ID = 0

fun NotificationManager.createNotification(
    message: String,
    applicationContext: Context,
    source: Int,
    code: Int,
) {
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    contentIntent.putExtra("source", source)
        .putExtra("code", code)
        .putExtra("TYPE", "download_notification")
        .flags = Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.download_notification_channel_id)
    ).apply {
        setSmallIcon(R.drawable.baseline_arrow_downward_24)
        setContentTitle("Download")
        setContentText(message)
        setAutoCancel(true)
        addAction(
            R.drawable.baseline_arrow_downward_24,
            applicationContext.getString(R.string.status),
            contentPendingIntent
        )
        priority = NotificationCompat.PRIORITY_HIGH
    }

    notify(NOTIFICATION_ID, builder.build())
}