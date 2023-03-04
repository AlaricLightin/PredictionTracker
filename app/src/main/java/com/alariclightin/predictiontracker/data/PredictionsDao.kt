package com.alariclightin.predictiontracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM predictions")
    fun getAllPredictions(): Flow<List<Prediction>>
}