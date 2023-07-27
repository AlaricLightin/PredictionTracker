package com.alariclightin.predictiontracker.data

import kotlinx.coroutines.flow.Flow

interface PredictionsExportRepository {
    fun getAllPredictions(): Flow<List<Prediction>>
}