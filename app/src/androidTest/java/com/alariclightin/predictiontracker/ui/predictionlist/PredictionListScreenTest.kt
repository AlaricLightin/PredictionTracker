package com.alariclightin.predictiontracker.ui.predictionlist

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.alariclightin.predictiontracker.R
import com.alariclightin.predictiontracker.data.Prediction
import com.alariclightin.predictiontracker.ui.theme.PredictionTrackerTheme
import com.alariclightin.predictiontracker.ui.utils.onNodeWithStringId
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.time.OffsetDateTime

class PredictionListScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val initRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var viewModel: PredictionListViewModel

//    @Before
//    fun setup() {
//
//    }

    @Test
    fun verifyEmptyContent() {
        setupWithList(emptyList())

        composeTestRule.onNodeWithStringId(R.string.no_predictions_description)
            .assertIsDisplayed()
    }

    @Test
    fun verifyValidPrediction() {
        setupWithList(
            listOf(
                Prediction(
                    text = "Some text",
                    probability = 90,
                    predictionDate = parseIsoDate("2015-06-01"),
                    resolveDate = parseIsoDate("2016-02-01"),
                    result = true
                )
            )
        )

        composeTestRule.onNodeWithText("Some text")
            .assertIsDisplayed()
    }

    private fun setupWithList(predictionList: List<Prediction>) {
        viewModel = mock(PredictionListViewModel::class.java)
        val predictionListUiState = MutableStateFlow(PredictionListUiState(predictionList))
        `when`(viewModel.predictionListUiState).thenReturn(predictionListUiState)

        composeTestRule.setContent {
            PredictionTrackerTheme {
                PredictionListScreen(
                    navigateToPredictionEntry = { },
                    viewModel = viewModel
                )
            }
        }
    }

    private fun parseIsoDate(text: String) =
        OffsetDateTime.parse("${text}T00:00:00+00")
}