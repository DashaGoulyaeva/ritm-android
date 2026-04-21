package com.dashagoulyaeva.ritm.core.database.di

import android.content.Context
import androidx.room.Room
import com.dashagoulyaeva.ritm.core.database.RitmDatabase
import com.dashagoulyaeva.ritm.core.database.dao.CycleDao
import com.dashagoulyaeva.ritm.core.database.dao.FastingDao
import com.dashagoulyaeva.ritm.core.database.dao.HabitDao
import com.dashagoulyaeva.ritm.core.database.dao.WaterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideRitmDatabase(
        @ApplicationContext context: Context,
    ): RitmDatabase =
        Room.databaseBuilder(
            context,
            RitmDatabase::class.java,
            "ritm.db",
        )
            .addMigrations(RitmDatabase.MIGRATION_1_2)
            .build()

    @Provides
    fun provideWaterDao(db: RitmDatabase): WaterDao = db.waterDao()

    @Provides
    fun provideHabitDao(db: RitmDatabase): HabitDao = db.habitDao()

    @Provides
    fun provideFastingDao(db: RitmDatabase): FastingDao = db.fastingDao()

    @Provides
    fun provideCycleDao(db: RitmDatabase): CycleDao = db.cycleDao()
}
