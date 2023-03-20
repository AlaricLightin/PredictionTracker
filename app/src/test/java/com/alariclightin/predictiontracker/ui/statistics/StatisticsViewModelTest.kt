@file:OptIn(ExperimentalCoroutinesApi::class)

package com.alariclightin.predictiontracker.ui.statistics

import com.alariclightin.predictiontracker.data.PredictionStatisticsRepository
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.offset
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class StatisticsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    @Parameters(method = "parametersForShouldCalculateUiState")
    fun shouldCalculateUiState(
        resultProbabilityList: List<Int>,
        expectedLogScore: Double,
        expectedMeanLogScore: Double,
        expectedBrierScore: Double,
        resultsCount: Int
    ) = runTest {
        val repository = object : PredictionStatisticsRepository {
            private val flow = MutableSharedFlow<List<Int>>()
            suspend fun emit(value: List<Int>) = flow.emit(value)
            override fun getResultProbabilityList(): Flow<List<Int>> = flow
        }

        val viewModel = StatisticsViewModel(repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.statisticsUiState.collect()
        }

        repository.emit(resultProbabilityList)

        assertThat(viewModel.statisticsUiState.value.resultsCount)
            .isEqualTo(resultsCount)

        assertThat(viewModel.statisticsUiState.value.logScore)
            .isCloseTo(expectedLogScore, offset(0.01))

        assertThat(viewModel.statisticsUiState.value.meanLogScore)
            .isCloseTo(expectedMeanLogScore, offset(0.01))

        assertThat(viewModel.statisticsUiState.value.brierScore)
            .isCloseTo(expectedBrierScore, offset(0.01))
    }

    fun parametersForShouldCalculateUiState(): Array<Array<Any>> =
        arrayOf(
            arrayOf(emptyList<Int>(), 0.0, 0.0, 0.0, 0),
            arrayOf(listOf(50), -0.693, -0.693, 0.25, 1),
            arrayOf(listOf(50, 50), -1.386, -0.693, 0.25, 2),
            arrayOf(listOf(10), -2.302, -2.302, 0.81, 1),
        )
}

// Reusable JUnit4 TestRule to override the Main dispatcher
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}