package com.alariclightin.predictiontracker.ui.prediction

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alariclightin.predictiontracker.R
import com.alariclightin.predictiontracker.data.Prediction

@Composable
fun PredictionDialogBody(
    prediction: Prediction,
    @StringRes questionId: Int
) {
    Column {
        Row {
            Text(stringResource(R.string.yourPrediction))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(prediction.text)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(stringResource(questionId))
        }
    }
}