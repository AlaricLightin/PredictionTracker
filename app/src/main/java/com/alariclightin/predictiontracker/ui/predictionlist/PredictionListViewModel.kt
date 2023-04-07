package com.alariclightin.predictiontracker.ui.predictionlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alariclightin.predictiontracker.data.Prediction
import com.alariclightin.predictiontracker.data.PredictionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class PredictionListViewModel @Inject constructor(
    private val predictionsRepository: PredictionsRepository
) : ViewModel() {

    val predictionListUiState: StateFlow<PredictionListUiState> = createPredictionListUiState()

    private fun createPredictionListUiState(): StateFlow<PredictionListUiState> {
        val currentDateTime = OffsetDateTime.now()
        val expiredPredictions = predictionsRepository.getExpiredPredictions(currentDateTime)
        val waitingForResolvePredictions = predictionsRepository.getWaitingForResolvePredictions(
            currentDateTime)
        val resolvedPredictions = predictionsRepository.getResolvedPredictions()

        return expiredPredictions.combine(
            waitingForResolvePredictions,
        ) { expired, waitingForResolve -> Pair(expired, waitingForResolve) }
            .combine(resolvedPredictions) { (expired, waitingForResolve), resolved ->
                PredictionListUiState(
                    expired,
                    waitingForResolve,
                    resolved
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PredictionListUiState(
                    listOf(), listOf(), listOf()
                )
            )

    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun resolvePrediction(prediction: Prediction, result: Boolean) {
        viewModelScope.launch {
            predictionsRepository.update(
                prediction.copy(result = result)
            )
        }
    }

    fun deletePrediction(prediction: Prediction) {
        viewModelScope.launch {
            predictionsRepository.delete(prediction)
        }
    }
}

data class PredictionListUiState(
    val timeExpiredList: List<Prediction>,
    val waitingForResolveList: List<Prediction>,
    val resolvedList: List<Prediction>
) {
    fun hasPredictions() =
        timeExpiredList.isNotEmpty() || waitingForResolveList.isNotEmpty() || resolvedList.isNotEmpty()
}