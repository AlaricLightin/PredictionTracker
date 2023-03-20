package com.alariclightin.predictiontracker.data

import kotlinx.coroutines.flow.Flow

interface PredictionStatisticsRepository {
    fun getResultProbabilityList(): Flow<List<Int>>
}