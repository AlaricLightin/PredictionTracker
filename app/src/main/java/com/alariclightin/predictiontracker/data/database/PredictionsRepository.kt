package com.alariclightin.predictiontracker.data.database

import com.alariclightin.predictiontracker.data.models.Prediction
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime

interface PredictionsRepository {
    fun getExpiredPredictions(currentDateTime: OffsetDateTime): Flow<List<Prediction>>

    fun getWaitingForResolvePredictions(currentDateTime: OffsetDateTime): Flow<List<Prediction>>

    fun getResolvedPredictions(): Flow<List<Prediction>>

    fun getItem(id: Int): Flow<Prediction?>

    suspend fun insert(prediction: Prediction)

    suspend fun delete(prediction: Prediction)

    suspend fun update(prediction: Prediction)
}