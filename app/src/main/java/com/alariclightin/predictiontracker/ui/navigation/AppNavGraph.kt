package com.alariclightin.predictiontracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alariclightin.predictiontracker.ui.prediction.PredictionEntryDestination
import com.alariclightin.predictiontracker.ui.prediction.PredictionEntryScreen
import com.alariclightin.predictiontracker.ui.predictionlist.MainScreen
import com.alariclightin.predictiontracker.ui.predictionlist.MainScreenDestination
import com.alariclightin.predictiontracker.ui.statistics.StatisticsDestination
import com.alariclightin.predictiontracker.ui.statistics.StatisticsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = MainScreenDestination.route,
        modifier = modifier
    ) {
        composable(route = MainScreenDestination.route) {
            MainScreen(
                navigateToPredictionEntry = {
                    navController.navigate(PredictionEntryDestination.route)
                },
                navigateToStatisticsScreen = {
                    navController.navigate(StatisticsDestination.route)
                }
            )
        }
        composable(route = PredictionEntryDestination.route) {
            PredictionEntryScreen(
                navigationDialogEvents = navController.getDialogEvents(),
            )
        }
        composable(route = StatisticsDestination.route) {
            StatisticsScreen(
                navigationDialogEvents = navController.getDialogEvents(),
            )
        }
    }
}

private fun NavController.getDialogEvents(): NavigationDialogEvents {
    return NavigationDialogEvents(
        onNavigateBack = { popBackStack() },
        onNavigateUp = { navigateUp() }
    )
}