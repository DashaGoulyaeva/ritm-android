package com.dashagoulyaeva.ritm.feature.habits.di

import com.dashagoulyaeva.ritm.feature.habits.data.repository.HabitRepositoryImpl
import com.dashagoulyaeva.ritm.feature.habits.domain.repository.HabitRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HabitDatabaseModule {
    @Binds
    @Singleton
    abstract fun bindHabitRepository(impl: HabitRepositoryImpl): HabitRepository
}
