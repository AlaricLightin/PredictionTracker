package com.alariclightin.predictiontracker.ui.predictionlist

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.filters.SdkSuppress
import com.alariclightin.predictiontracker.R
import com.alariclightin.predictiontracker.data.Prediction
import com.alariclightin.predictiontracker.ui.TestTagConsts
import com.alariclightin.predictiontracker.ui.theme.PredictionTrackerTheme
import com.alariclightin.predictiontracker.ui.utils.onNodeWithStringId
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.time.OffsetDateTime

@SdkSuppress(minSdkVersion = Build.VERSION_CODES.P)
@RunWith(JUnitParamsRunner::class)
class PredictionListScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val initRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var viewModel: PredictionListViewModel

    @Test
    fun verifyEmptyContent() {
        setupWithList(emptyList())

        composeTestRule.onNodeWithStringId(R.string.no_predictions_description)
            .assertIsDisplayed()
    }

    @Test
    @Parameters(
        value = [
            "50, true, , Result: correct",
            "50, false, , Result: incorrect",
            "90, true, +8.5, Result: correct",
            "90, false, -23.2, Result: incorrect",
            "10, false, +8.5, Result: incorrect",
        ]
    )
    fun verifyPredictionWithResultProperties(
        probability: Int,
        predictionResult: Boolean,
        expectedScore: String,
        expectedResultText: String
    ) {
        setupWithList(
            listOf(
                Prediction(
                    text = "Some text",
                    probability = probability,
                    predictionDate = parseIsoDate("2015-06-01"),
                    resolveDate = parseIsoDate("2016-02-01"),
                    result = predictionResult
                )
            )
        )

        composeTestRule.onNodeWithText("Some text")
            .assertIsDisplayed()

        if (probability != 50) {
            composeTestRule.onNodeWithTag(TestTagConsts.PredictionScoreText, true)
                .assertIsDisplayed()
                .assert(hasText(expectedScore))
        }
        else
            composeTestRule.onNodeWithTag(TestTagConsts.PredictionScoreText, true)
                .assertIsNotDisplayed()

        composeTestRule.onNodeWithTag(TestTagConsts.PredictionResultText, true)
            .assertIsDisplayed()
            .assert(hasText(expectedResultText))
    }

    fun verifyPredictionWithoutResult() {
        setupWithList(
            listOf(
                Prediction(
                    text = "Some text",
                    probability = 50,
                    predictionDate = parseIsoDate("2015-06-01"),
                    resolveDate = parseIsoDate("2016-02-01"),
                    result = null
                )
            )
        )

        composeTestRule.onNodeWithText("Some text")
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag(TestTagConsts.PredictionScoreText, true)
            .assertIsNotDisplayed()

        composeTestRule.onNodeWithTag(TestTagConsts.PredictionResultText, true)
            .assertIsNotDisplayed()
    }

    private fun setupWithList(predictionList: List<Prediction>) {
        viewModel = mock(PredictionListViewModel::class.java)
        val predictionListUiState = MutableStateFlow(PredictionListUiState(predictionList))
        `when`(viewModel.predictionListUiState).thenReturn(predictionListUiState)

        composeTestRule.setContent {
            PredictionTrackerTheme {
                PredictionListScreen(
                    navigateToPredictionEntry = { },
                    navigateToStatisticsScreen = { },
                    viewModel = viewModel
                )
            }
        }
    }

    private fun parseIsoDate(text: String) =
        OffsetDateTime.parse("${text}T00:00:00+00")
}