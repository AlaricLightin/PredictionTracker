package com.alariclightin.predictiontracker.ui.predictionlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alariclightin.predictiontracker.R
import com.alariclightin.predictiontracker.data.models.Prediction
import com.alariclightin.predictiontracker.ui.TestTagConsts
import com.alariclightin.predictiontracker.ui.prediction.*
import com.alariclightin.predictiontracker.ui.theme.cardBackgroundColors
import com.alariclightin.predictiontracker.ui.theme.scoreTextColors
import java.time.OffsetDateTime

data class PredictionEvents(
    val onResolve: (Prediction, Boolean) -> Unit = { _, _ -> },
    val onDelete: (Prediction) -> Unit = { }
)

@Composable
fun PredictionListComponent(
    predictionList: List<Prediction>,
    title: String,
    modifier: Modifier = Modifier,
    predictionEvents: PredictionEvents = PredictionEvents()
) {
    if (predictionList.isEmpty())
        return

    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
            )

            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = predictionList, key = { it.id }) { prediction ->
                    PredictionElement(
                        prediction = prediction,
                        predictionEvents = predictionEvents
                    )
                }
            }
        }
    }
}

@Composable
fun PredictionElement(
    prediction: Prediction,
    predictionEvents: PredictionEvents,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        var menuExpanded by remember { mutableStateOf(false) }
        var resolveDialogOpened by remember { mutableStateOf(false) }
        var deleteDialogOpened by remember { mutableStateOf(false) }

        val colorType = prediction.getCardColorType()

        Card(
            modifier = modifier
                .clickable { menuExpanded = true }
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.cardBackgroundColors[colorType]
            )
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 8.dp)
                ) {
                    Row {
                        Text(
                            text = prediction.predictionString()
                        )
                        Text(text = " ")
                        Text(text = prediction.text)
                    }

                    Row {
                        Text(text = prediction.datesString(stringResource(R.string.prediction_dates)))
                    }

                    if (prediction.result != null) {
                        Row {
                            Text(
                                text = if (prediction.result) stringResource(R.string.correct_result)
                                else stringResource(R.string.incorrect_result),
                                modifier = modifier
                                    .testTag(TestTagConsts.PredictionResultText)
                            )
                        }
                    }
                } // end Column with text

                Spacer(modifier = modifier.width(8.dp))

                Box(
                    modifier = modifier
                        .padding(vertical = 8.dp)
                        .width(60.dp)
                        .fillMaxHeight()
                ) {
                    if (prediction.result != null)
                        Text(
                            text = prediction.getScoreString(),
                            color = MaterialTheme.colorScheme.scoreTextColors[colorType],
                            modifier = modifier
                                .align(Alignment.Center)
                                .testTag(TestTagConsts.PredictionScoreText)
                        )
                } // end Box with score
            } // end Row
        } // end Card

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            if (prediction.result == null) {
                DropdownMenuItem(
                    onClick = { resolveDialogOpened = true },
                    text = { Text(text = stringResource(R.string.resolve_prediction)) }
                )
            }
            DropdownMenuItem(
                onClick = { deleteDialogOpened = true },
                text = { Text(text = stringResource(R.string.delete)) }
            )
        }

        if (resolveDialogOpened) {
            ResolvePredictionDialog(
                prediction = prediction,
                onResolve = {
                    resolveDialogOpened = false
                    menuExpanded = false
                    predictionEvents.onResolve(prediction, it)
                },
                onDismiss = {
                    resolveDialogOpened = false
                    menuExpanded = false
                }
            )
        }

        if (deleteDialogOpened) {
            DeletePredictionDialog(
                prediction = prediction,
                onDelete = {
                    deleteDialogOpened = false
                    menuExpanded = false
                    predictionEvents.onDelete(prediction)
                },
                onCancel = {
                    deleteDialogOpened = false
                    menuExpanded = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PredictionListComponentPreview() {
    fun parseIsoDate(text: String) =
        OffsetDateTime.parse("${text}T00:00:00+00")

    PredictionListComponent(
        predictionList = listOf(
            Prediction(
                id = 1,
                text = "Prediction 1",
                probability = 60,
                predictionDateTime = parseIsoDate("2021-01-01"),
                resolveDateTime = parseIsoDate("2021-01-31"),
                result = true
            ),

            Prediction(
                id = 2,
                text = "Prediction 2",
                probability = 70,
                predictionDateTime = parseIsoDate("2021-01-01"),
                resolveDateTime = parseIsoDate("2022-01-31"),
                result = false
            )
        ),
        title = "Predictions",
    )
}