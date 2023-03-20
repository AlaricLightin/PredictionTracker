package com.alariclightin.predictiontracker

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.alariclightin.predictiontracker.ui.navigation.AppNavHost

@Composable
fun PredictionTrackerApp(navController: NavHostController = rememberNavController()) {
    AppNavHost(navController = navController)
}
