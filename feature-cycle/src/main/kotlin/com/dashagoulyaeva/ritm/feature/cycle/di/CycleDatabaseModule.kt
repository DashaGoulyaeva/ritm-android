package com.dashagoulyaeva.ritm.feature.cycle.di

import com.dashagoulyaeva.ritm.feature.cycle.data.repository.CycleRepositoryImpl
import com.dashagoulyaeva.ritm.feature.cycle.domain.repository.CycleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CycleDatabaseModule {

    @Binds
    @Singleton
    abstract fun bindCycleRepository(impl: CycleRepositoryImpl): CycleRepository
}
