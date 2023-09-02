package com.alariclightin.predictiontracker.data.settings

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class SettingsModule {
    @Binds
    abstract fun provideDefaultResolveDateRepository(
        impl: DefaultResolveDateRepositoryImpl
    ): DefaultResolveDateRepository
}