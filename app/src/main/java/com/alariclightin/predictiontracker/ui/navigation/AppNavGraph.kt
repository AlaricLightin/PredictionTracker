package com.alariclightin.predictiontracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alariclightin.predictiontracker.ui.prediction.PredictionEntryDestination
import com.alariclightin.predictiontracker.ui.prediction.PredictionEntryScreen
import com.alariclightin.predictiontracker.ui.predictionlist.PredictionListDestination
import com.alariclightin.predictiontracker.ui.predictionlist.PredictionListScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = PredictionListDestination.route,
        modifier = modifier
    ) {
        composable(route = PredictionListDestination.route) {
            PredictionListScreen(
                navigateToPredictionEntry = {
                    navController.navigate(PredictionEntryDestination.route)
                }
            )
        }
        composable(route = PredictionEntryDestination.route) {
            PredictionEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}