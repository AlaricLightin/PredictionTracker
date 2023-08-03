package com.alariclightin.predictiontracker.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alariclightin.predictiontracker.data.database.PredictionStatisticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.math.ln

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    predictionStatisticsRepository: PredictionStatisticsRepository
): ViewModel() {
    val statisticsUiState: StateFlow<StatisticsUiState> = predictionStatisticsRepository
        .getResultProbabilityList()
        .map { p -> p.map { it.toDouble() / 100 } }
        .map { probabilityList ->
            val resultsCount = probabilityList.size
            val logScore = probabilityList.sumOf { ln(it) }
            val logMeanScore = if (resultsCount > 0) {
                logScore / resultsCount
            } else {
                0.0
            }
            val brierScore = if (resultsCount > 0) {
                probabilityList.sumOf { (1.0 - it) * (1.0 - it) } / resultsCount
            } else {
                0.0
            }
            StatisticsUiState(
                logScore = logScore,
                meanLogScore = logMeanScore,
                brierScore = brierScore,
                resultsCount = resultsCount
            )
        }
        .stateIn (
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = StatisticsUiState(
                0.0, 0.0, 0.0, 0
            )
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class StatisticsUiState(
    val logScore: Double,
    val meanLogScore: Double,
    val brierScore: Double,
    val resultsCount: Int,
)