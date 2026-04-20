package com.dashagoulyaeva.ritm.feature.water.di

import com.dashagoulyaeva.ritm.core.database.RitmDatabase
import com.dashagoulyaeva.ritm.core.database.dao.WaterDao
import com.dashagoulyaeva.ritm.feature.water.data.repository.WaterRepositoryImpl
import com.dashagoulyaeva.ritm.feature.water.domain.repository.WaterRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WaterDatabaseModule {
    @Binds
    @Singleton
    abstract fun bindWaterRepository(impl: WaterRepositoryImpl): WaterRepository

    companion object {
        @Provides
        @Singleton
        fun provideWaterDao(db: RitmDatabase): WaterDao = db.waterDao()
    }
}
