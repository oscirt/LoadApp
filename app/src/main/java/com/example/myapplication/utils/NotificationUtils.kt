package com.example.myapplication.utils

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.myapplication.R

private const val NOTIFICATION_ID = 0

fun NotificationManager.createNotification(
    message: String,
    applicationContext: Context,
    args: Bundle
) {
    val contentPendingIntent = NavDeepLinkBuilder(applicationContext)
        .setGraph(R.navigation.nav_graph)
        .addDestination(R.id.downloadStatusFragment, args)
        .createPendingIntent()

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