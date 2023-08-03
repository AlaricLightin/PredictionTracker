package com.alariclightin.predictiontracker.ui.prediction

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.alariclightin.predictiontracker.R
import com.alariclightin.predictiontracker.data.models.Prediction

@Composable
fun DeletePredictionDialog(
    prediction: Prediction,
    onDelete: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.delete_prediction)) },
        text = {
            PredictionDialogBody(prediction = prediction, questionId = R.string.delete_question)
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDelete) {
                Text(stringResource(R.string.yes))
            }
        }
    )
}