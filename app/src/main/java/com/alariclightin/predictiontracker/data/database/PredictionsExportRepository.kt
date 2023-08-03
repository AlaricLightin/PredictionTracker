package com.alariclightin.predictiontracker.data.database

import com.alariclightin.predictiontracker.data.models.Prediction
import kotlinx.coroutines.flow.Flow

interface PredictionsExportRepository {
    fun getAllPredictions(): Flow<List<Prediction>>
}