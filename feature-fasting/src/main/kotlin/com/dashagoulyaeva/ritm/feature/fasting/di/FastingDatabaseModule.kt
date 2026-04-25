package com.dashagoulyaeva.ritm.feature.fasting.di

import com.dashagoulyaeva.ritm.feature.fasting.data.repository.FastingRepositoryImpl
import com.dashagoulyaeva.ritm.feature.fasting.domain.repository.FastingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FastingDatabaseModule {
    @Binds
    @Singleton
    abstract fun bindFastingRepository(impl: FastingRepositoryImpl): FastingRepository
}
