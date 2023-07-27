package com.alariclightin.predictiontracker.ui.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alariclightin.predictiontracker.data.PredictionsExportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileWriter
import javax.inject.Inject

@HiltViewModel
class OptionsViewModel @Inject constructor(
    private val predictionsExportRepository: PredictionsExportRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {
    private val _exportFileUiState = MutableStateFlow(ExportFileUiState(null, true))
    val exportFileUiState: StateFlow<ExportFileUiState> = _exportFileUiState.asStateFlow()

    fun exportDataToFile(
        file: File
    ) {
        viewModelScope.launch {
            predictionsExportRepository.getAllPredictions()
                .conflate()
                .map { predictionList ->
                    Json.encodeToString(predictionList)
                }
                .flowOn(ioDispatcher)
                .collect { json ->
                    FileWriter(file).use {
                        it.write(json)
                    }
                    _exportFileUiState.value = ExportFileUiState(file, false)
                }
        }
    }

    fun resetExportState() {
        _exportFileUiState.value.file?.delete()
        _exportFileUiState.value = ExportFileUiState(null, true)
    }
}

data class ExportFileUiState(
    val file: File?,
    val isButtonEnabled: Boolean
)