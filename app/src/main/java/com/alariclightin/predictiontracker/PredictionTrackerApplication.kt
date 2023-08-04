package com.alariclightin.predictiontracker

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.alariclightin.predictiontracker.notifications.createNotificationChannels
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PredictionTrackerApplication: Application(), Configuration.Provider {
    @Inject
    lateinit var hiltWorkerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels(context = this)
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build()
}