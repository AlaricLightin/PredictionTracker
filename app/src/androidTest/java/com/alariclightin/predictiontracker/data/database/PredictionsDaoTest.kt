package com.alariclightin.predictiontracker.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alariclightin.predictiontracker.data.models.Prediction
import com.alariclightin.predictiontracker.sharedtest.getTestPrediction
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.OffsetDateTime
import java.time.ZoneOffset

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnitParamsRunner::class)
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

    @Suppress("JUnitMalformedDeclaration")
    @Test
    @Parameters(method = "parametersForDaoResultProbabilityList")
    fun daoGetResultProbabilityList(
        predictionList: List<Prediction>,
        expectedResultList: List<Int>
    ) = runTest {
        addPredictions(predictionList)
        val result = dao.getResultProbabilityList().first()
        assertEquals(expectedResultList, result)
    }

    @Suppress("unused")
    private fun parametersForDaoResultProbabilityList(): List<Array<Any>> = listOf(
        arrayOf(
            listOf(
                getTestPrediction(probability = 30, result = true),
                getTestPrediction(probability = 60, result = null),
                getTestPrediction(probability = 40, result = false)
            ),
            listOf(30, 60)
        ),
        arrayOf(
            listOf(
                getTestPrediction(probability = 30, result = null),
                getTestPrediction(probability = 60, result = null),
                getTestPrediction(probability = 40, result = null)
            ),
            emptyList<Int>()
        )
    )

    @Suppress("JUnitMalformedDeclaration")
    @Test
    @Parameters(method = "parametersForDaoGetExpiredPredictionList")
    fun daoGetExpiredPredictionList(
        predictionList: List<Prediction>,
        currentDateTime: OffsetDateTime,
        expectedResultList: List<Int>
    ) = runTest {
        addPredictions(predictionList)
        val result = dao.getExpiredPredictions(currentDateTime).first()
        assertEquals(expectedResultList, result.map { it.id })
    }

    @Suppress("unused")
    private fun parametersForDaoGetExpiredPredictionList(): List<Array<Any>> = listOf(
        arrayOf(
            getListForGetPredictionTesting(),
            OffsetDateTime.now(),
            listOf(5, 2)
        ),
        arrayOf(
            listOf(
                getTestPrediction(
                    id = 1,
                    resolveDate = OffsetDateTime.of(
                        2021, 1, 1, 12, 0, 0, 0,
                        ZoneOffset.UTC
                    )
                ),
                getTestPrediction(
                    id = 2,
                    resolveDate = OffsetDateTime.of(
                        2021, 1, 1, 14, 0, 0, 0,
                        ZoneOffset.of("+3")
                    )
                )
            ),

            OffsetDateTime.of(
                2021, 1, 1, 13, 0, 0, 0,
                ZoneOffset.UTC
            ),

            listOf(2, 1)
        )
    )

    @Suppress("JUnitMalformedDeclaration")
    @Test
    @Parameters(method = "parametersForDaoGetWaitingForResolvePredictions")
    fun getWaitingForResolveList(
        predictionList: List<Prediction>,
        currentDateTime: OffsetDateTime,
        expectedResultList: List<Int>
    ) = runTest {
        addPredictions(predictionList)
        val result = dao.getWaitingForResolvePredictions(currentDateTime).first()
        assertEquals(expectedResultList, result.map { it.id })
    }

    @Suppress("unused")
    private fun parametersForDaoGetWaitingForResolvePredictions(): List<Array<Any>> = listOf(
        arrayOf(
            getListForGetPredictionTesting(),
            OffsetDateTime.now(),
            listOf(4)
        ),
        arrayOf(
            listOf(
                getTestPrediction(
                    id = 1,
                    resolveDate = OffsetDateTime.of(
                        2021, 1, 1, 12, 0, 0, 0,
                        ZoneOffset.UTC
                    )
                ),
                getTestPrediction(
                    id = 2,
                    resolveDate = OffsetDateTime.of(
                        2021, 1, 1, 14, 0, 0, 0,
                        ZoneOffset.of("+3")
                    )
                )
            ),

            OffsetDateTime.of(
                2021, 1, 1, 13, 0, 0, 0,
                ZoneOffset.UTC
            ),

            emptyList<Int>()
        ),

        arrayOf(
            listOf(
                getTestPrediction(
                    id = 1,
                    resolveDate = OffsetDateTime.of(
                        2021, 1, 1, 12, 0, 0, 0,
                        ZoneOffset.UTC
                    )
                ),
                getTestPrediction(
                    id = 2,
                    resolveDate = OffsetDateTime.of(
                        2021, 1, 1, 14, 0, 0, 0,
                        ZoneOffset.of("+3")
                    )
                ),
                getTestPrediction(
                    id = 3,
                    resolveDate = OffsetDateTime.of(
                        2021, 1, 1, 7, 0, 0, 0,
                        ZoneOffset.of("+3")
                    )
                )
            ),

            OffsetDateTime.of(
                2021, 1, 1, 8, 0, 0, 0,
                ZoneOffset.UTC
            ),

            listOf(2, 1)
        )
    )

    @Suppress("JUnitMalformedDeclaration")
    @Test
    @Parameters(method = "parametersForDaoGetResolvedPredictions")
    fun getResolvedList(
        predictionList: List<Prediction>,
        expectedResultList: List<Int>
    ) = runTest {
        addPredictions(predictionList)
        val result = dao.getResolvedPredictions().first()
        assertEquals(expectedResultList, result.map { it.id })
    }

    @Suppress("unused")
    private fun parametersForDaoGetResolvedPredictions(): List<Array<Any>> = listOf(
        arrayOf(
            getListForGetPredictionTesting(),
            listOf(3, 1)
        ),

        arrayOf(
            listOf(
                getTestPrediction(
                    id = 1,
                    resolveDate = OffsetDateTime.of(
                        2021, 1, 1, 12, 0, 0, 0,
                        ZoneOffset.UTC
                    ),
                    result = true
                ),
                getTestPrediction(
                    id = 2,
                    resolveDate = OffsetDateTime.of(
                        2021, 1, 1, 14, 0, 0, 0,
                        ZoneOffset.of("+3")
                    ),
                    result = false
                ),
                getTestPrediction(
                    id = 3,
                    resolveDate = OffsetDateTime.of(
                        2021, 1, 1, 7, 0, 0, 0,
                        ZoneOffset.of("+3")
                    ),
                    result = true
                )
            ),

            listOf(1, 2, 3)
        )
    )

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