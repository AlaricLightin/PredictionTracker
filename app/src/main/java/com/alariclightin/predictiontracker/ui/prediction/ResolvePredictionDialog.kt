package com.alariclightin.predictiontracker.ui.prediction

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alariclightin.predictiontracker.R
import com.alariclightin.predictiontracker.data.Prediction
import com.alariclightin.predictiontracker.ui.theme.PredictionTrackerTheme
import java.time.OffsetDateTime

@Composable
fun ResolvePredictionDialog(
    prediction: Prediction,
    onResolve: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.addPredictionResult))
        },
        text = {
            PredictionDialogBody(
                prediction = prediction,
                questionId = R.string.wasPredictionCorrect
            )
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = { onResolve(true) }) {
                    Text(stringResource(R.string.yes))
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = { onResolve(false) }) {
                    Text(stringResource(R.string.no))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PredictionResolveDialogPreview() {
    PredictionTrackerTheme {
        ResolvePredictionDialog(
            prediction = Prediction(
                1, "Will human settlement be on Mars in 2030?", 60,
                predictionDate = OffsetDateTime.parse("2022-01-01T00:00:00+00"),
                resolveDate = OffsetDateTime.parse("2031-01-01T00:00:00+00")
            ),
            onResolve = { },
            onDismiss = { }
        )
    }
}