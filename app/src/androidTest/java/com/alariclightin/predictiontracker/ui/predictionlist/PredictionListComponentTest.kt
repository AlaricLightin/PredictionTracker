package com.alariclightin.predictiontracker.ui.predictionlist

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.filters.SdkSuppress
import com.alariclightin.predictiontracker.sharedtest.TEST_PREDICTION_TEXT
import com.alariclightin.predictiontracker.sharedtest.getTestPrediction
import com.alariclightin.predictiontracker.ui.TestTagConsts
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SdkSuppress(minSdkVersion = Build.VERSION_CODES.P)
@RunWith(JUnitParamsRunner::class)
class PredictionListComponentTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun verifyEmptyList() {
        composeTestRule.setContent {
            PredictionListComponent(emptyList(), "List title")
        }

        composeTestRule
            .onNodeWithText("List title")
            .assertDoesNotExist()
    }

    @Test
    fun verifyListTitleWhenListIsNotEmpty() {
        composeTestRule.setContent {
            PredictionListComponent(listOf(getTestPrediction()), "List title")
        }

        composeTestRule
            .onNodeWithText("List title")
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
    @Suppress("JUnitMalformedDeclaration")
    fun verifyPredictionCardProperties(
        probability: Int,
        predictionResult: Boolean,
        expectedScore: String,
        expectedResultText: String
    ) {
        composeTestRule.setContent {
            PredictionListComponent(listOf(
                getTestPrediction(probability = probability, result = predictionResult)
            ), "List title")
        }

        composeTestRule.onNodeWithText(TEST_PREDICTION_TEXT)
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

    fun verifyPredictionWithoutResultCardProperties() {
        composeTestRule.setContent {
            PredictionListComponent(listOf(
                getTestPrediction(probability = 80, result = null)
            ), "List title")
        }

        composeTestRule.onNodeWithText("Some text")
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag(TestTagConsts.PredictionScoreText, true)
            .assertIsNotDisplayed()

        composeTestRule.onNodeWithTag(TestTagConsts.PredictionResultText, true)
            .assertIsNotDisplayed()
    }

}