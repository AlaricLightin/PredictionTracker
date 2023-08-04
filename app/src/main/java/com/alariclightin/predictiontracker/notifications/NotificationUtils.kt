package com.alariclightin.predictiontracker.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.alariclightin.predictiontracker.R

const val RESOLVE_CHANNEL = "resolve_channel"
const val RESOLVE_NOTIFICATION_ID = 1

fun createNotificationChannels(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val mChannel = NotificationChannel(
            RESOLVE_CHANNEL,
            context.getString(R.string.resolve_prediction_notification),
            NotificationManager.IMPORTANCE_DEFAULT
        )

        // TODO description

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager?.createNotificationChannel(mChannel)
    }
}
