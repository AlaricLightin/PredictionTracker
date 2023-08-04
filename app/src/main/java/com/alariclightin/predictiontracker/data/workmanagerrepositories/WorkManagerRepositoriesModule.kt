package com.alariclightin.predictiontracker.data.workmanagerrepositories

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkManagerRepositoriesModule {
    @Binds
    abstract fun provideResolveNotification(impl: ResolveNotificationRepositoryImpl): ResolveNotificationRepository
}