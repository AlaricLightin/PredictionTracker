package com.alariclightin.predictiontracker.ui.predictionlist

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.filters.SdkSuppress
import com.alariclightin.predictiontracker.R
import com.alariclightin.predictiontracker.data.models.Prediction
import com.alariclightin.predictiontracker.sharedtest.getTestPrediction
import com.alariclightin.predictiontracker.ui.theme.PredictionTrackerTheme
import com.alariclightin.predictiontracker.ui.utils.onNodeWithStringId
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.time.OffsetDateTime

@SdkSuppress(minSdkVersion = Build.VERSION_CODES.P)
class MainScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val initRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var viewModel: PredictionListViewModel

    @Test
    fun verifyEmptyContent() {
        setupWithList()

        composeTestRule.onNodeWithStringId(R.string.no_predictions_description)
            .assertIsDisplayed()
    }

    @Test
    fun verifyListTitlesForNonEmptyContent() {
        setupWithList(
            timeExpiredList = listOf(
                Prediction(
                    id = 1,
                    text = "Prediction 1",
                    probability = 50,
                    result = true,
                    predictionDateTime = OffsetDateTime.now().minusDays(5),
                    resolveDateTime = OffsetDateTime.now().minusDays(3),
                )
            ),
            waitingForResolveList = listOf(
                getTestPrediction(probability = 70, result = null)
            ),
            resolvedList = listOf(
                getTestPrediction(probability = 90, result = false)
            )
        )

        composeTestRule.onNodeWithStringId(R.string.add_results)
            .assertIsDisplayed()

        composeTestRule.onNodeWithStringId(R.string.waiting_for_resolve)
            .assertIsDisplayed()

        composeTestRule.onNodeWithStringId(R.string.past_predictions)
            .assertIsDisplayed()
    }


    private fun setupWithList(
        timeExpiredList: List<Prediction> = emptyList(),
        waitingForResolveList: List<Prediction> = emptyList(),
        resolvedList: List<Prediction> = emptyList()
    ) {
        viewModel = mock(PredictionListViewModel::class.java)
        val predictionListUiState = MutableStateFlow(PredictionListUiState(
            timeExpiredList = timeExpiredList,
            waitingForResolveList = waitingForResolveList,
            resolvedList = resolvedList
        ))
        `when`(viewModel.predictionListUiState).thenReturn(predictionListUiState)

        composeTestRule.setContent {
            PredictionTrackerTheme {
                MainScreen(
                    navigateToPredictionEntry = { },
                    navigateToStatisticsScreen = { },
                    navigateToOptionsScreen = { },
                    viewModel = viewModel
                )
            }
        }
    }
}