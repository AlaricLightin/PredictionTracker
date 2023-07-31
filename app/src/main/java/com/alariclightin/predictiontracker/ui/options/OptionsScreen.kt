package com.alariclightin.predictiontracker.ui.options

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.alariclightin.predictiontracker.R
import com.alariclightin.predictiontracker.ui.navigation.DialogAppBar
import com.alariclightin.predictiontracker.ui.navigation.NavigationDestination
import com.alariclightin.predictiontracker.ui.navigation.NavigationDialogEvents
import com.alariclightin.predictiontracker.ui.theme.PredictionTrackerTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object OptionsDestination : NavigationDestination {
    override val route: String = "options"
}

@Composable
fun OptionsScreen(
    navigationDialogEvents: NavigationDialogEvents,
    modifier: Modifier = Modifier,
    viewModel: OptionsViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current.applicationContext
    val cacheDir = remember(context, lifecycleOwner) {
        context.cacheDir
    }
    val exportFileUiState by viewModel.exportFileUiState.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.resetExportState()
    }

    Scaffold(
        topBar = {
            DialogAppBar(
                titleRes = R.string.options,
                navigateUp = navigationDialogEvents.onNavigateUp
            )
        }
    ) { innerPadding ->
        OptionsBody(
            onExportData = {
                val jsonFile = File(cacheDir, createTimestampedFilename())
                viewModel.exportDataToFile(jsonFile)
            },
            exportButtonEnabled = exportFileUiState.isButtonEnabled,
            modifier = modifier
                .padding(innerPadding)
        )
    }

    exportFileUiState.file?.let { file ->
        val uri = FileProvider.getUriForFile(
            context,
            "com.alariclightin.predictiontracker.fileprovider", file
        )

        LaunchedEffect(uri) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            launcher.launch(Intent.createChooser(intent, "Share File"))
        }
    }
}

private fun createTimestampedFilename(): String {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    return "predictions_$timeStamp.json"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsBody(
    onExportData: () -> Unit,
    exportButtonEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = onExportData,
            enabled = exportButtonEnabled
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.export_data)
                )

                // TODO write explanation
            }
        }
    }
}

@Preview
@Composable
fun OptionsBodyPreview() {
    PredictionTrackerTheme {
        OptionsBody(
            onExportData = {},
            exportButtonEnabled = true
        )
    }
}