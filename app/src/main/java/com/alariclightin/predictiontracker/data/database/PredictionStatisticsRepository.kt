package com.alariclightin.predictiontracker.data.database

import kotlinx.coroutines.flow.Flow

interface PredictionStatisticsRepository {
    fun getResultProbabilityList(): Flow<List<Int>>
}