package com.alariclightin.predictiontracker.ui.prediction

import com.alariclightin.predictiontracker.data.Prediction
import com.alariclightin.predictiontracker.ui.theme.CardColorType
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.FormatStyle
import kotlin.math.log2

data class PredictionUiState(
    val id: Int = 0,
    val text: String = "",
    val probability: String = "",
    val resolveDate: String = "",
    val result: Boolean? = null,
    val actionEnabled: Boolean = false
)

fun PredictionUiState.toPrediction(): Prediction = Prediction(
    id = id,
    text = text,
    probability = probability.toIntOrNull() ?: 50,
    // FIXME add time to resolveDate
    resolveDate = localDateToOffsetDateTime(resolveDate),
    predictionDate = OffsetDateTime.now(),
    result = result
)

private fun localDateToOffsetDateTime(localDateStr: String): OffsetDateTime {
    val localDate = LocalDateTime.parse("${localDateStr}T00:00:00")
    return localDate.atOffset(
        ZoneId.systemDefault().rules.getOffset(localDate))
}

fun PredictionUiState.isValid(): Boolean {
    if (text.isBlank())
        return false

    val probabilityInt = probability.toIntOrNull()
    if (probabilityInt == null || probabilityInt < 1 || probabilityInt > 99)
        return false

    try {
        val localDate = LocalDate.parse(resolveDate, DateTimeFormatter.ISO_LOCAL_DATE)
        if (localDate < LocalDate.now())
            return false
    }
    catch (e: DateTimeParseException) {
        return false
    }

    return true
}

fun Prediction.predictionString() = String.format("%d%%", probability)

fun Prediction.datesString(formatString: String): String {
    val dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
    return String.format(
        formatString,
        predictionDate.format(dateTimeFormatter),
        resolveDate.format(dateTimeFormatter)
    )
}

enum class PredictionRightness{
    WRONG,
    NEUTRAL,
    RIGHT
}

fun Prediction.getRightness(): PredictionRightness? {
    if (result == null)
        return null

    return when {
        probability == 50 -> PredictionRightness.NEUTRAL
        result.xor(probability > 50) -> PredictionRightness.WRONG
        else -> PredictionRightness.RIGHT
    }
}

fun Prediction.getScoreString(): String {
    if (result == null || probability == 50)
        return ""

    val score = if (result) log2(2 * probability.toDouble() / 100) * 10
    else log2(2 * (100 - probability).toDouble() / 100) * 10

    val decimalFormat = DecimalFormat("+0.0;-0.0")
    return decimalFormat.format(score)
}

fun Prediction.getCardColorType(): CardColorType {
    val rightness = getRightness()
    return when{
        rightness == PredictionRightness.RIGHT ->
            CardColorType.RightAnswer
        rightness == PredictionRightness.WRONG ->
            CardColorType.WrongAnswer
        result == null && resolveDate < OffsetDateTime.now() ->
            CardColorType.Expired
        else -> CardColorType.Normal
    }
}