package com.alariclightin.predictiontracker.data.database

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class) // TODO вернуть ViewModelComponent, когда разберёмся, что внедрять в Worker
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