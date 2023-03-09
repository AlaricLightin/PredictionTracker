package com.alariclightin.predictiontracker.ui.predictionlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alariclightin.predictiontracker.PredictionTrackerTopAppBar
import com.alariclightin.predictiontracker.R
import com.alariclightin.predictiontracker.data.Prediction
import com.alariclightin.predictiontracker.ui.navigation.NavigationDestination
import com.alariclightin.predictiontracker.ui.prediction.*
import com.alariclightin.predictiontracker.ui.theme.*
import java.time.OffsetDateTime

object PredictionListDestination : NavigationDestination {
    override val route: String = "predictionList"
    override val titleRes: Int = R.string.predictionList
}

data class PredictionEvents(
    val onResolve: (Prediction, Boolean) -> Unit = { _, _ -> },
    val onDelete: (Prediction) -> Unit = { }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredictionListScreen(
    navigateToPredictionEntry: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PredictionListViewModel = hiltViewModel()
) {
    val predictionListUiState by viewModel.predictionListUiState.collectAsState()

    Scaffold(
        topBar = {
            PredictionTrackerTopAppBar(
                title = stringResource(PredictionListDestination.titleRes),
                canNavigateBack = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToPredictionEntry,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_prediction),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        PredictionListBody(
            predictionList = predictionListUiState.predictionList,
            predictionEvents = PredictionEvents(
                onResolve = { prediction, result ->
                    viewModel.resolvePrediction(prediction, result)
                },
                onDelete = { viewModel.deletePrediction(it) }
            ),
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun PredictionListBody(
    predictionList: List<Prediction>,
    predictionEvents: PredictionEvents,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (predictionList.isNotEmpty()) {
            PredictionList(
                predictionList = predictionList,
                predictionEvents = predictionEvents
            )
        } else {
            Text(
                text = stringResource(R.string.no_predictions_description),
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@Composable
fun PredictionList(
    predictionList: List<Prediction>,
    predictionEvents: PredictionEvents,
    modifier: Modifier = Modifier
) {
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

        Card(
            modifier = modifier
                .clickable { menuExpanded = true }
                .fillMaxWidth()
                .padding(8.dp),
            elevation = cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.cardColors[prediction.getCardColorType()]
            )
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
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
                                else stringResource(R.string.incorrect_result)
                            )
                        }
                    }
                } // end Column
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
private fun PredictionListPreview() {
    fun parseIsoDate(text: String) =
        OffsetDateTime.parse("${text}T00:00:00+00")

    PredictionTrackerTheme {
        PredictionListBody(
            predictionList = listOf(
                Prediction(
                    1, "Will human settlement be on Mars in 2030?", 60,
                    predictionDate = OffsetDateTime.parse("2022-01-01T00:00:00+00"),
                    resolveDate = OffsetDateTime.parse("2031-01-01T00:00:00+00")
                ),
                Prediction(
                    2, "Will Trump be President of the US in 2016?", 30,
                    predictionDate = parseIsoDate("2015-06-01"),
                    resolveDate = parseIsoDate("2016-02-01"),
                    result = true
                ),
                Prediction(
                    3, "Will Trump be President of the US in 2020?", 40,
                    predictionDate = parseIsoDate("2019-06-01"),
                    resolveDate = parseIsoDate("2020-02-01"),
                    result = false
                ),
                Prediction(
                    4, "Will Biden be President of the US in 2020?", 60,
                    predictionDate = parseIsoDate("2019-06-01"),
                    resolveDate = parseIsoDate("2020-02-01")
                )
            ),
            predictionEvents = PredictionEvents()
        )
    }
}