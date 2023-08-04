package com.alariclightin.predictiontracker.data.workmanagerrepositories

import java.time.OffsetDateTime

interface ResolveNotificationRepository {
    fun setNotification(time: OffsetDateTime)
}