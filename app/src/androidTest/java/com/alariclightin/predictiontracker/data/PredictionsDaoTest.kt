package com.alariclightin.predictiontracker.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.OffsetDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class PredictionsDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: PredictionsDao

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room
            .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = db.predictionsDao()
    }

    @Test
    fun daoGetResultProbabilityList() = runTest {
        addPredictions(
            listOf(
                getTestPrediction(30, true),
                getTestPrediction(60, null),
                getTestPrediction(40, false)
            )
        )
        val result = dao.getResultProbabilityList().first()
        assertEquals(listOf(30, 60), result)
    }

    private suspend fun addPredictions(predictions: List<Prediction>) {
        predictions.forEach { dao.insert(it) }
    }

    private fun getTestPrediction(probability: Int, result: Boolean?) =
        Prediction(
            0, "SomeText", probability,
            predictionDate = OffsetDateTime.parse("2022-01-01T00:00:00+00"),
            resolveDate = OffsetDateTime.parse("2031-01-01T00:00:00+00"),
            result = result
        )
}