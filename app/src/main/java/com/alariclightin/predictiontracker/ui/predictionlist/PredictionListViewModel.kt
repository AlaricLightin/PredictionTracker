package com.alariclightin.predictiontracker.ui.predictionlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alariclightin.predictiontracker.data.Prediction
import com.alariclightin.predictiontracker.data.PredictionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PredictionListViewModel @Inject constructor(
    private val predictionsRepository: PredictionsRepository
) : ViewModel() {

    val predictionListUiState: StateFlow<PredictionListUiState> = predictionsRepository
        .getAllPredictionsStream()
        .map { PredictionListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = PredictionListUiState()
        )

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

data class PredictionListUiState(val predictionList: List<Prediction> = listOf())