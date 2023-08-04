package com.alariclightin.predictiontracker.data.database

import androidx.room.*
import com.alariclightin.predictiontracker.data.models.Prediction
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

    @Query(
        """
        SELECT * FROM predictions 
        WHERE result IS NULL AND resolveDateTime < :currentDateTime 
        ORDER BY resolveDateTime ASC
        """
    )
    fun getExpiredPredictions(currentDateTime: OffsetDateTime): Flow<List<Prediction>>

    @Query(
        """
        SELECT * FROM predictions 
        WHERE result IS NULL AND resolveDateTime >= :currentDateTime 
        ORDER BY resolveDateTime ASC
        """
    )
    fun getWaitingForResolvePredictions(currentDateTime: OffsetDateTime): Flow<List<Prediction>>

    @Query(
        """
        SELECT * FROM predictions 
        WHERE result IS NOT NULL 
        ORDER BY resolveDateTime DESC
        """
    )
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

    @Query(
        """
        SELECT * FROM predictions
        ORDER BY resolveDateTime DESC
    """
    )
    fun getAllPredictions(): Flow<List<Prediction>>
}