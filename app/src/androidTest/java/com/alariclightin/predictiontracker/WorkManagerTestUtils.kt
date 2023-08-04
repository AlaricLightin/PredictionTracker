package com.alariclightin.predictiontracker

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper

fun workManagerInitialize() {
    val context = InstrumentationRegistry.getInstrumentation().getTargetContext()
    val config = Configuration.Builder()
        .setMinimumLoggingLevel(Log.DEBUG)
        .setExecutor(SynchronousExecutor())
        .build()

    // Initialize WorkManager for instrumentation tests.
    WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
}