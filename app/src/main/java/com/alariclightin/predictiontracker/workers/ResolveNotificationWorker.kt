package com.alariclightin.predictiontracker.workers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alariclightin.predictiontracker.MainActivity
import com.alariclightin.predictiontracker.R
import com.alariclightin.predictiontracker.data.database.PredictionsRepository
import com.alariclightin.predictiontracker.notifications.RESOLVE_CHANNEL
import com.alariclightin.predictiontracker.notifications.RESOLVE_NOTIFICATION_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ResolveNotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    predictionsRepository: PredictionsRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val intent = Intent(context, MainActivity::class.java).apply { // TODO remove MainActivity dependency
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, RESOLVE_CHANNEL)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.add_prediction_result))
            .setContentText("Time to add prediction result")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        // TODO fix NOTIFICATION_ID
        // TODO permission check
        NotificationManagerCompat.from(context).notify(RESOLVE_NOTIFICATION_ID, builder.build())
        return Result.success()
    }
}