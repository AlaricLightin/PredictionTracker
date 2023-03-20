package com.alariclightin.predictiontracker.ui.prediction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alariclightin.predictiontracker.R
import com.alariclightin.predictiontracker.ui.navigation.DialogAppBar
import com.alariclightin.predictiontracker.ui.navigation.NavigationDestination
import com.alariclightin.predictiontracker.ui.navigation.NavigationDialogEvents
import com.alariclightin.predictiontracker.ui.theme.PredictionTrackerTheme
import kotlinx.coroutines.launch

object PredictionEntryDestination: NavigationDestination {
    override val route: String = "prediction_entry"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredictionEntryScreen(
    navigationDialogEvents: NavigationDialogEvents,
    modifier: Modifier = Modifier,
    viewModel: PredictionEntryViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            DialogAppBar(
                titleRes = R.string.prediction_entry_title,
                navigateUp = navigationDialogEvents.onNavigateUp
            )
        }
    ) { innerPadding ->
        PredictionEntryBody(
            predictionUiState = viewModel.predictionUiState,
            onValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.save()
                    navigationDialogEvents.onNavigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun PredictionEntryBody(
    predictionUiState: PredictionUiState,
    onValueChange: (PredictionUiState) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        PredictionInputForm(
            predictionUiState = predictionUiState,
            onValueChange = onValueChange,
        )
        Button(
            onClick = onSaveClick,
            enabled = predictionUiState.actionEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredictionInputForm(
    predictionUiState: PredictionUiState,
    onValueChange: (PredictionUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = predictionUiState.text,
            onValueChange = { onValueChange(predictionUiState.copy(text = it)) },
            label = { Text(stringResource(R.string.question)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        OutlinedTextField(
            value = predictionUiState.probability,
            onValueChange = { onValueChange(predictionUiState.copy(probability = it)) },
            label = { Text(stringResource(R.string.probability)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = predictionUiState.resolveDate,
            onValueChange = { onValueChange(predictionUiState.copy(resolveDate = it)) },
            label = { Text(stringResource(R.string.resolve_date)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PredictionEntryScreenPreview() {
    PredictionTrackerTheme {
        PredictionEntryBody(
            predictionUiState = PredictionUiState(
                text = "Will be human settlement on Mars in 2030?",
                probability = "60",
                resolveDate = "2031-01-01"
            ),
            onSaveClick = {},
            onValueChange = {}
        )
    }
}