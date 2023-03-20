package com.alariclightin.predictiontracker.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflinePredictionsRepository @Inject constructor(
    private val predictionsDao: PredictionsDao
) : PredictionsRepository, PredictionStatisticsRepository {

    override fun getAllPredictionsStream(): Flow<List<Prediction>> =
        predictionsDao.getAllPredictions()

    override fun getItemStream(id: Int): Flow<Prediction?> = predictionsDao.getPrediction(id)

    override suspend fun insert(prediction: Prediction) = predictionsDao.insert(prediction)

    override suspend fun delete(prediction: Prediction) = predictionsDao.delete(prediction)

    override suspend fun update(prediction: Prediction) = predictionsDao.update(prediction)

    override fun getResultProbabilityList(): Flow<List<Int>> =
        predictionsDao.getResultProbabilityList()
}