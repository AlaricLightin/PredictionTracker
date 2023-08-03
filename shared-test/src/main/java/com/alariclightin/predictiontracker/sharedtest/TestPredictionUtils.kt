package com.alariclightin.predictiontracker.sharedtest

import com.alariclightin.predictiontracker.data.models.Prediction
import java.time.OffsetDateTime

const val TEST_PREDICTION_TEXT = "SomeText"

fun getTestPrediction(
    id: Int = 0,
    text: String = TEST_PREDICTION_TEXT,
    probability: Int = 50,
    predictionDate: OffsetDateTime = OffsetDateTime.parse("2022-01-01T00:00:00+00"),
    resolveDate: OffsetDateTime = OffsetDateTime.parse("2031-01-01T00:00:00+00"),
    result: Boolean? = null
) = Prediction(
    id, text, probability,
    predictionDate = predictionDate,
    resolveDate = resolveDate,
    result = result
)