package com.dashagoulyaeva.ritm.di

import com.dashagoulyaeva.ritm.core.common.WaterReminderScheduler
import com.dashagoulyaeva.ritm.reminder.WaterReminderSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReminderModule {
    @Binds
    @Singleton
    abstract fun bindWaterReminderScheduler(impl: WaterReminderSchedulerImpl): WaterReminderScheduler
}
