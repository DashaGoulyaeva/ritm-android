package com.dashagoulyaeva.ritm.feature.water.di

import com.dashagoulyaeva.ritm.feature.water.data.repository.WaterGoalRepositoryImpl
import com.dashagoulyaeva.ritm.feature.water.domain.repository.WaterGoalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WaterPreferencesModule {
    @Binds
    @Singleton
    abstract fun bindWaterGoalRepository(impl: WaterGoalRepositoryImpl): WaterGoalRepository
}
