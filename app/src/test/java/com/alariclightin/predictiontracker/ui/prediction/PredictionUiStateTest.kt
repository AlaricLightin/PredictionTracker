package com.alariclightin.predictiontracker.ui.prediction

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class PredictionUiStateTest {

    @Test
    @Parameters(
        value = [
            "Some text, 50, 2199-01-01, true",
            ", 50, 2021-01-01, false",
            "Some text, 0, 2021-01-01, false",
            "Some text, 102, 2021-01-01, false",
            "Some text, 50, 2021-01-01T00:00:00, false",
            "Some text, bbb, 2021-01-01, false",
            "Some text, 50, 2020-01-01, false",
        ]
    )
    fun shouldGetValidity(
        text: String,
        probability: String,
        resolveDate: String,
        expectedResult: Boolean
    ) {
        val predictionUiState = PredictionUiState(
            text = text,
            probability = probability,
            resolveDate = resolveDate
        )

        assertThat(predictionUiState.isValid())
            .isEqualTo(expectedResult)
    }
}