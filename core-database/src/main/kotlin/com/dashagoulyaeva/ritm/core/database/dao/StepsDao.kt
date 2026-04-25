package com.dashagoulyaeva.ritm.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dashagoulyaeva.ritm.core.database.entity.StepDailyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StepsDao {
    @Query("SELECT * FROM step_daily_logs WHERE date = :date LIMIT 1")
    fun getStepsForDate(date: String): Flow<StepDailyEntity?>

    @Query("SELECT * FROM step_daily_logs ORDER BY date DESC LIMIT :limit")
    fun getRecentSteps(limit: Int): Flow<List<StepDailyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSteps(entity: StepDailyEntity)
}
