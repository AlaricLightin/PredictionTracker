package com.alariclightin.predictiontracker.ui.options

import com.alariclightin.predictiontracker.data.Prediction
import com.alariclightin.predictiontracker.data.PredictionsExportRepository
import com.alariclightin.predictiontracker.sharedtest.TEST_PREDICTION_TEXT
import com.alariclightin.predictiontracker.sharedtest.getTestPrediction
import com.alariclightin.predictiontracker.ui.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

@OptIn(ExperimentalCoroutinesApi::class)
class OptionsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun shouldExportDataToFile() {
        val tempDirectory: Path = Files.createTempDirectory("test")
        try {
            runTest {
                val repository = object : PredictionsExportRepository {
                    private val flow = MutableSharedFlow<List<Prediction>>()
                    suspend fun emit(value: List<Prediction>) = flow.emit(value)
                    override fun getAllPredictions(): Flow<List<Prediction>> = flow
                }

                val viewModel = OptionsViewModel(repository, UnconfinedTestDispatcher())
                val resultFile: File = tempDirectory.resolve("test.json").toFile()

                viewModel.exportDataToFile(resultFile)
                repository.emit(listOf(getTestPrediction(id = 1, result = true)))
                println("OptionsViewModelTest - After emit")
                assertThat(viewModel.exportFileUiState.value.file)
                    .isFile
                    .exists()
                    .content()
                    .contains(TEST_PREDICTION_TEXT)

                assertThat(viewModel.exportFileUiState.value.isButtonEnabled)
                    .isFalse()
            }
        }
        finally {
            tempDirectory.toFile().deleteRecursively()
        }
    }
}