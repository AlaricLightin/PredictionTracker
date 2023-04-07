package com.alariclightin.predictiontracker.data

import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime
import javax.inject.Inject

class OfflinePredictionsRepository @Inject constructor(
    private val predictionsDao: PredictionsDao
) : PredictionsRepository, PredictionStatisticsRepository {

    override fun getExpiredPredictions(currentDateTime: OffsetDateTime): Flow<List<Prediction>> =
        predictionsDao.getExpiredPredictions(currentDateTime)

    override fun getWaitingForResolvePredictions(currentDateTime: OffsetDateTime)
            : Flow<List<Prediction>> =
        predictionsDao.getWaitingForResolvePredictions(currentDateTime)

    override fun getResolvedPredictions(): Flow<List<Prediction>> =
        predictionsDao.getResolvedPredictions()

    override fun getItem(id: Int): Flow<Prediction?> = predictionsDao.getPrediction(id)

    override suspend fun insert(prediction: Prediction) = predictionsDao.insert(prediction)

    override suspend fun delete(prediction: Prediction) = predictionsDao.delete(prediction)

    override suspend fun update(prediction: Prediction) = predictionsDao.update(prediction)

    override fun getResultProbabilityList(): Flow<List<Int>> =
        predictionsDao.getResultProbabilityList()
}