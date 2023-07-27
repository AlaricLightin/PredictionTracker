package com.alariclightin.predictiontracker.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alariclightin.predictiontracker.sharedtest.getTestPrediction
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
                getTestPrediction(probability = 30, result = true),
                getTestPrediction(probability = 60, result = null),
                getTestPrediction(probability = 40, result = false)
            )
        )
        val result = dao.getResultProbabilityList().first()
        assertEquals(listOf(30, 60), result)
    }

    @Test
    fun daoGetResultProbabilityListWithNoResults() = runTest {
        addPredictions(
            listOf(
                getTestPrediction(probability = 30, result = null),
                getTestPrediction(probability = 60, result = null),
                getTestPrediction(probability = 40, result = null)
            )
        )
        val result = dao.getResultProbabilityList().first()
        assertEquals(emptyList<Int>(), result)
    }

    @Test
    fun daoGetExpiredPredictionList() = runTest {
        addPredictions(getListForGetPredictionTesting())
        val result = dao.getExpiredPredictions(OffsetDateTime.now()).first()
        assertEquals(listOf(5, 2), result.map { it.id })
    }

    @Test
    fun getWaitingForResolveList() = runTest {
        addPredictions(getListForGetPredictionTesting())
        val result = dao.getWaitingForResolvePredictions(OffsetDateTime.now()).first()
        assertEquals(listOf(4), result.map { it.id })
    }

    @Test
    fun getResolvedList() = runTest {
        addPredictions(getListForGetPredictionTesting())
        val result = dao.getResolvedPredictions().first()
        assertEquals(listOf(3, 1), result.map { it.id })
    }

    private fun getListForGetPredictionTesting() =
        listOf(
            getTestPrediction(
                id = 1,
                probability = 30,
                result = true,
                resolveDate = OffsetDateTime.now().minusDays(4)
            ),
            getTestPrediction(
                id = 2,
                probability = 60,
                result = null,
                resolveDate = OffsetDateTime.now().minusDays(3)
            ),
            getTestPrediction(
                id = 3,
                probability = 40,
                result = false,
                resolveDate = OffsetDateTime.now().minusDays(3)
            ),
            getTestPrediction(
                id = 4,
                probability = 40,
                result = null,
                resolveDate = OffsetDateTime.now().plusDays(3)
            ),
            getTestPrediction(
                id = 5,
                probability = 40,
                result = null,
                resolveDate = OffsetDateTime.now().minusDays(5)
            )
        )


    private suspend fun addPredictions(predictions: List<Prediction>) {
        predictions.forEach { dao.insert(it) }
    }
}