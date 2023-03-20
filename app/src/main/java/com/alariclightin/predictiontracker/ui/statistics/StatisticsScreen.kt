package com.alariclightin.predictiontracker.ui.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alariclightin.predictiontracker.R
import com.alariclightin.predictiontracker.ui.navigation.DialogAppBar
import com.alariclightin.predictiontracker.ui.navigation.NavigationDestination
import com.alariclightin.predictiontracker.ui.navigation.NavigationDialogEvents
import com.alariclightin.predictiontracker.ui.theme.PredictionTrackerTheme

object StatisticsDestination: NavigationDestination {
    override val route: String = "statistics"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navigationDialogEvents: NavigationDialogEvents,
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val statisticsUiState by viewModel.statisticsUiState.collectAsState()
    Scaffold(
        topBar = {
            DialogAppBar(
                titleRes = R.string.statistics,
                navigateUp = navigationDialogEvents.onNavigateUp
            )
        }
    ) { innerPadding ->
        StatisticsBody(
            statisticsUiState = statisticsUiState,
            modifier = modifier
                .padding(innerPadding)
        )
    }
}

@Composable
fun StatisticsBody(
    statisticsUiState: StatisticsUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.accuracy),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = stringResource(R.string.total_results, statisticsUiState.resultsCount)
        )

        Text(
            text = stringResource(R.string.log_score, statisticsUiState.logScore)
        )

        Text(
            text = stringResource(R.string.mean_log_score, statisticsUiState.meanLogScore)
        )

        Text(
            text = stringResource(R.string.brier_score, statisticsUiState.brierScore)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsBodyPreview() {
    PredictionTrackerTheme {
        StatisticsBody(
            statisticsUiState = StatisticsUiState(
                -1.386, -0.693, 0.25, 2
            )
        )
    }
}
