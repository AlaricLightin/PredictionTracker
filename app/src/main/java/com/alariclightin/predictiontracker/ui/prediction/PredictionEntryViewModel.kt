package com.alariclightin.predictiontracker.ui.prediction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.alariclightin.predictiontracker.data.database.PredictionsRepository
import com.alariclightin.predictiontracker.data.settings.DefaultResolveDateRepository
import com.alariclightin.predictiontracker.data.workmanagerrepositories.ResolveNotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PredictionEntryViewModel @Inject constructor(
    private val predictionsRepository: PredictionsRepository,
    private val resolveNotificationRepository: ResolveNotificationRepository,
    defaultResolveDateRepository: DefaultResolveDateRepository
): ViewModel() {
    var predictionUiState by mutableStateOf(PredictionUiState(
        resolveDate = defaultResolveDateRepository.getDefaultResolveDate(),
        resolveTime = defaultResolveDateRepository.getDefaultResolveTime()
    ))
        private set

    fun updateUiState(newState: PredictionUiState) {
        predictionUiState = newState.copy( actionEnabled = newState.isValid() )
    }

    suspend fun save() {
        if (predictionUiState.isValid()) {
            val prediction = predictionUiState.toPrediction()
            predictionsRepository.insert(prediction)
            resolveNotificationRepository.setNotification(prediction.resolveDateTime)
        }
    }
}