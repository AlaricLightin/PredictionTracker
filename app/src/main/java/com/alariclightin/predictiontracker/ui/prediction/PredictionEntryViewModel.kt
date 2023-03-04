package com.alariclightin.predictiontracker.ui.prediction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.alariclightin.predictiontracker.data.PredictionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PredictionEntryViewModel @Inject constructor(
    private val predictionsRepository: PredictionsRepository
): ViewModel() {
    var predictionUiState by mutableStateOf(PredictionUiState())
        private set

    fun updateUiState(newState: PredictionUiState) {
        predictionUiState = newState.copy( actionEnabled = newState.isValid() )
    }

    suspend fun save() {
        if (predictionUiState.isValid())
            predictionsRepository.insert(predictionUiState.toPrediction())
    }
}