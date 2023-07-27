package com.alariclightin.predictiontracker.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providePredictions(impl: OfflinePredictionsRepository): PredictionsRepository

    @Binds
    abstract fun providePredictionStatistics(
        impl: OfflinePredictionsRepository
    ): PredictionStatisticsRepository

    @Binds
    abstract fun providePredictionsExport(
        impl: OfflinePredictionsRepository
    ): PredictionsExportRepository
}