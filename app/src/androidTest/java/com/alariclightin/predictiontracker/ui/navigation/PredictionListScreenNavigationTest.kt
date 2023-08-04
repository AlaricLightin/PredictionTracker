package com.alariclightin.predictiontracker.ui.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.alariclightin.predictiontracker.MainActivity
import com.alariclightin.predictiontracker.PredictionTrackerApp
import com.alariclightin.predictiontracker.R
import com.alariclightin.predictiontracker.ui.TestTagConsts
import com.alariclightin.predictiontracker.ui.utils.assertNodeWithTestTagContainsTextEqualsToTextWithStringId
import com.alariclightin.predictiontracker.workManagerInitialize
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class PredictionListScreenNavigationTest {
    @get:Rule(order = 1)
    val hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setupAppNavHost() {
        workManagerInitialize()

        hiltTestRule.inject()
        composeTestRule.activity.setContent {
            PredictionTrackerApp()
        }
    }

    @Test
    fun appNavHost_verifyStartDestination() {
        composeTestRule
            .assertNodeWithTestTagContainsTextEqualsToTextWithStringId(
                TestTagConsts.TopAppBarText, R.string.predictionList)
    }

    @Test
    fun appNavHost_clickFab_navigatesToPredictionEntryScreen() {
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.add_prediction)
        ).performClick()

        composeTestRule
            .assertNodeWithTestTagContainsTextEqualsToTextWithStringId(
                TestTagConsts.TopAppBarText, R.string.prediction_entry_title
            )
    }

    @Test
    fun appNavHost_clickStatistics_navigatesToStatisticsScreen() {
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.menu)
        ).performClick()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.statistics)
        ).performClick()

        composeTestRule
            .assertNodeWithTestTagContainsTextEqualsToTextWithStringId(
                TestTagConsts.TopAppBarText, R.string.statistics
            )
    }

    @Test
    fun appNavHost_clickOptions_navigatesToOptionsScreen() {
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.menu)
        ).performClick()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.options)
        ).performClick()

        composeTestRule
            .assertNodeWithTestTagContainsTextEqualsToTextWithStringId(
                TestTagConsts.TopAppBarText, R.string.options
            )

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(R.string.export_data)
            )
            .assertIsDisplayed()
    }
}