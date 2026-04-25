package com.dashagoulyaeva.ritm.feature.home.di

import com.dashagoulyaeva.ritm.feature.home.data.StepsRepositoryImpl
import com.dashagoulyaeva.ritm.feature.home.domain.repository.StepsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StepsDatabaseModule {
    @Binds
    @Singleton
    abstract fun bindStepsRepository(impl: StepsRepositoryImpl): StepsRepository
}
