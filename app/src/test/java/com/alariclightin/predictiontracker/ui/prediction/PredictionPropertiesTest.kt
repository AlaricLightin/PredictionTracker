package com.alariclightin.predictiontracker.ui.prediction

import com.alariclightin.predictiontracker.data.models.Prediction
import com.alariclightin.predictiontracker.ui.theme.CardColorType
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.time.OffsetDateTime

@RunWith(JUnitParamsRunner::class)
internal class PredictionPropertiesTest {

    @Suppress("JUnitMalformedDeclaration")
    @Test
    @Parameters(value = [
        "50, true, Normal",
        "50, false, Normal",
        "90, true, RightAnswer",
        "90, false, WrongAnswer",
        "10, false, RightAnswer",
        "10, true, WrongAnswer",
    ])
    fun shouldGetCardColorTypeRightOrWrongAnswer(
        probability: Int,
        predictionResult: Boolean,
        expectedResult: CardColorType
    ) {
        val prediction = Prediction(
            text = "Some prediction text",
            probability = probability,
            predictionDateTime = OffsetDateTime.now().minusDays(10),
            resolveDateTime = OffsetDateTime.now().minusDays(1),
            result = predictionResult
        )

        assertThat(prediction.getCardColorType())
            .isEqualTo(expectedResult)
    }

    @Test
    fun shouldGetCardColorTypeExpired() {
        val prediction = Prediction(
            text = "Some prediction text",
            probability = 70,
            predictionDateTime = OffsetDateTime.now().minusDays(10),
            resolveDateTime = OffsetDateTime.now().minusDays(1),
            result = null
        )

        assertThat(prediction.getCardColorType())
            .isEqualTo(CardColorType.Expired)
    }

    @Test
    fun shouldGetCardColorTypeNormalForNonExpired() {
        val prediction = Prediction(
            text = "Some prediction text",
            probability = 70,
            predictionDateTime = OffsetDateTime.now().minusDays(10),
            resolveDateTime = OffsetDateTime.now().plusDays(1),
            result = null
        )

        assertThat(prediction.getCardColorType())
            .isEqualTo(CardColorType.Normal)
    }
}