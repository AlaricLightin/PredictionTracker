package com.alariclightin.predictiontracker.ui.prediction

import com.alariclightin.predictiontracker.data.database.PredictionsRepository
import com.alariclightin.predictiontracker.data.settings.DefaultResolveDateRepository
import com.alariclightin.predictiontracker.data.workmanagerrepositories.ResolveNotificationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
internal class PredictionEntryViewModelTest {

    @Mock
    private lateinit var predictionsRepository: PredictionsRepository

    @Mock
    private lateinit var resolveNotificationRepository: ResolveNotificationRepository

    @Test
    fun shouldInitializePredictionUiState() {
        val dateResult = "2021-01-01"
        val timeResult = "17:17"
        val defaultResolveDateRepository = object : DefaultResolveDateRepository {
            override fun getDefaultResolveDate(): String {
                return dateResult
            }

            override fun getDefaultResolveTime(): String {
                return timeResult
            }
        }

        val viewModel = PredictionEntryViewModel(
            predictionsRepository,
            resolveNotificationRepository,
            defaultResolveDateRepository
        )

        val predictionUiState: PredictionUiState = viewModel.predictionUiState

        assertThat(predictionUiState)
            .hasFieldOrPropertyWithValue("resolveDate", dateResult)
            .hasFieldOrPropertyWithValue("resolveTime", timeResult)
    }

}