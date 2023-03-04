package com.alariclightin.predictiontracker.data

import kotlinx.coroutines.flow.Flow

interface PredictionsRepository {
    fun getAllPredictionsStream(): Flow<List<Prediction>>

    fun getItemStream(id: Int): Flow<Prediction?>

    suspend fun insert(prediction: Prediction)

    suspend fun delete(prediction: Prediction)

    suspend fun update(prediction: Prediction)
}