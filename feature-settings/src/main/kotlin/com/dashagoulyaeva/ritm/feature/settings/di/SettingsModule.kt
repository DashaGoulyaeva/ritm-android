package com.dashagoulyaeva.ritm.feature.settings.di

import com.dashagoulyaeva.ritm.feature.settings.data.repository.WaterSettingsRepositoryImpl
import com.dashagoulyaeva.ritm.feature.settings.domain.repository.WaterSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {
    @Binds
    @Singleton
    abstract fun bindWaterSettingsRepository(impl: WaterSettingsRepositoryImpl): WaterSettingsRepository
}
