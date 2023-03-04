package com.alariclightin.predictiontracker.ui.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.alariclightin.predictiontracker.MainActivity
import com.alariclightin.predictiontracker.PredictionTrackerApp
import com.alariclightin.predictiontracker.R
import com.alariclightin.predictiontracker.ui.TestTagConsts
import com.alariclightin.predictiontracker.ui.prediction.PredictionEntryDestination
import com.alariclightin.predictiontracker.ui.predictionlist.PredictionListDestination
import com.alariclightin.predictiontracker.ui.utils.assertNodeWithTestTagContainsTextEqualsToTextWithStringId
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
        hiltTestRule.inject()
        composeTestRule.activity.setContent {
            PredictionTrackerApp()
        }
    }

    @Test
    fun appNavHost_verifyStartDestination() {
        composeTestRule
            .assertNodeWithTestTagContainsTextEqualsToTextWithStringId(
                TestTagConsts.TopAppBarText, PredictionListDestination.titleRes)
    }

    @Test
    fun appNavHost_clickFab_navigatesToPredictionEntryScreen() {
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.add_prediction)
        ).performClick()

        composeTestRule
            .assertNodeWithTestTagContainsTextEqualsToTextWithStringId(
                TestTagConsts.TopAppBarText, PredictionEntryDestination.titleRes
            )
    }
}