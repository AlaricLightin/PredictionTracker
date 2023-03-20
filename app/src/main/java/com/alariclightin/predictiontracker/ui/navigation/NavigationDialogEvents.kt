package com.alariclightin.predictiontracker.ui.navigation

data class NavigationDialogEvents(
    val onNavigateBack: () -> Unit,
    val onNavigateUp: () -> Unit,
)
