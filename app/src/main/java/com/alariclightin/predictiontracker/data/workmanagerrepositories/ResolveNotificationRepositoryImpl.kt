package com.alariclightin.predictiontracker.data.workmanagerrepositories

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.alariclightin.predictiontracker.workers.ResolveNotificationWorker
import com.alariclightin.predictiontracker.workers.TAG_RESOLVE_NOTIFICATION
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.OffsetDateTime
import javax.inject.Inject

class ResolveNotificationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): ResolveNotificationRepository {

    private val workManager = WorkManager.getInstance(context)

    override fun setNotification(time: OffsetDateTime) {
        val workRequest = OneTimeWorkRequestBuilder<ResolveNotificationWorker>()
            .addTag(TAG_RESOLVE_NOTIFICATION)
            .setInitialDelay(Duration.between(OffsetDateTime.now(), time))
            .build()

        workManager.enqueue(workRequest)
    }
}
