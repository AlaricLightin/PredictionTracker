package com.alariclightin.predictiontracker.data.settings

import java.time.LocalDate
import javax.inject.Inject

class DefaultResolveDateRepositoryImpl @Inject constructor() : DefaultResolveDateRepository {
    override fun getDefaultResolveDate(): String {
        return LocalDate.now().plusDays(1).toString()
    }

    override fun getDefaultResolveTime(): String {
        return "00:00"
    }
}