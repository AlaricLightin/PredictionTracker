package com.alariclightin.predictiontracker.ui.predictionlist

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alariclightin.predictiontracker.R
import com.alariclightin.predictiontracker.data.Prediction
import com.alariclightin.predictiontracker.ui.TestTagConsts
import com.alariclightin.predictiontracker.ui.navigation.NavigationDestination
import com.alariclightin.predictiontracker.ui.prediction.*
import com.alariclightin.predictiontracker.ui.theme.*
import java.time.OffsetDateTime

object MainScreenDestination : NavigationDestination {
    override val route: String = "predictionList"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navigateToPredictionEntry: () -> Unit,
    navigateToStatisticsScreen: () -> Unit,
    navigateToOptionsScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PredictionListViewModel = hiltViewModel()
) {
    val predictionListUiState by viewModel.predictionListUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.predictionList),
                        modifier = modifier.testTag(TestTagConsts.TopAppBarText)
                    )
                },

                actions = {
                    MainMenu {
                        DropdownMenuItem(
                            onClick = navigateToStatisticsScreen,
                            text = { Text(text = stringResource(id = R.string.statistics)) }
                        )

                        DropdownMenuItem(
                            onClick = navigateToOptionsScreen,
                            text = { Text(text = stringResource(id = R.string.options)) }
                        )
                    }
                }
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
        MainScreenBody(
            predictionListUiState = predictionListUiState,
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
fun MainScreenBody(
    predictionListUiState: PredictionListUiState,
    predictionEvents: PredictionEvents,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (predictionListUiState.hasPredictions()) {
            PredictionListComponent(
                predictionList = predictionListUiState.timeExpiredList,
                title = stringResource(id = R.string.add_results),
                predictionEvents = predictionEvents
            )

            PredictionListComponent(
                predictionList = predictionListUiState.waitingForResolveList,
                title  = stringResource(id = R.string.waiting_for_resolve),
                predictionEvents = predictionEvents
            )

            PredictionListComponent(
                predictionList = predictionListUiState.resolvedList,
                title = stringResource(id = R.string.past_predictions),
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

@Preview(showBackground = true)
@Composable
private fun MainScreenBodyPreview() {
    fun parseIsoDate(text: String) =
        OffsetDateTime.parse("${text}T00:00:00+00")

    PredictionTrackerTheme {
        MainScreenBody(
            predictionListUiState = PredictionListUiState(
                timeExpiredList = listOf(
                    Prediction(
                        1, "I will finish this app by March 2023", 10,
                        predictionDate = parseIsoDate("2022-01-01"),
                        resolveDate = parseIsoDate("2023-03-01")
                    )
                ),

                waitingForResolveList = listOf(
                    Prediction(
                        2, "Human settlement will be on Mars by 2030", 60,
                        predictionDate = parseIsoDate("2021-01-01"),
                        resolveDate = parseIsoDate("2031-01-01")
                    )
                ),

                resolvedList = listOf(
                    Prediction(
                        3, "Trump will be elected as President of the US in 2016", 30,
                        predictionDate = parseIsoDate("2015-06-01"),
                        resolveDate = parseIsoDate("2016-02-01"),
                        result = true
                    ),
                    Prediction(
                        4, "GPT-4 will be released in 2023", 70,
                        predictionDate = parseIsoDate("2019-06-01"),
                        resolveDate = parseIsoDate("2024-01-01"),
                        result = true
                    )
                )
            ),

            predictionEvents = PredictionEvents()
        )
    }
}