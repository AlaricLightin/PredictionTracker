package com.alariclightin.predictiontracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime

@Dao
interface PredictionsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(prediction: Prediction)

    @Update
    suspend fun update(prediction: Prediction)

    @Delete
    suspend fun delete(prediction: Prediction)

    @Query("SELECT * FROM predictions WHERE id = :id")
    fun getPrediction(id: Int): Flow<Prediction>

    @Query("SELECT * FROM predictions " +
            "WHERE result IS NULL AND resolveDate < :currentDateTime " +
            "ORDER BY resolveDate ASC")
    fun getExpiredPredictions(currentDateTime: OffsetDateTime): Flow<List<Prediction>>

    @Query("SELECT * FROM predictions " +
            "WHERE result IS NULL AND resolveDate >= :currentDateTime " +
            "ORDER BY resolveDate ASC")
    fun getWaitingForResolvePredictions(currentDateTime: OffsetDateTime): Flow<List<Prediction>>

    @Query("SELECT * FROM predictions " +
            "WHERE result IS NOT NULL " +
            "ORDER BY resolveDate DESC")
    fun getResolvedPredictions(): Flow<List<Prediction>>

    @Query("""
        SELECT
            CASE result
                WHEN 1 THEN probability
                ELSE 100 - probability
            END AS resultProbability
        FROM predictions
        WHERE result IS NOT NULL
    """)
    fun getResultProbabilityList(): Flow<List<Int>>
}