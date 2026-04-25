package com.dashagoulyaeva.ritm.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dashagoulyaeva.ritm.core.database.entity.CycleDayLogEntity
import com.dashagoulyaeva.ritm.core.database.entity.CyclePeriodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CycleDao {
    @Query("SELECT * FROM cycle_periods ORDER BY startDate DESC")
    fun getAllPeriods(): Flow<List<CyclePeriodEntity>>

    @Query("SELECT * FROM cycle_periods ORDER BY startDate DESC LIMIT :limit")
    suspend fun getRecentPeriods(limit: Int): List<CyclePeriodEntity>

    @Query("SELECT * FROM cycle_periods WHERE endDate IS NULL ORDER BY startDate DESC LIMIT 1")
    fun getActivePeriod(): Flow<CyclePeriodEntity?>

    @Query("SELECT * FROM cycle_periods WHERE endDate IS NULL ORDER BY startDate DESC LIMIT 1")
    suspend fun getActivePeriodOnce(): CyclePeriodEntity?

    @Insert
    suspend fun insertPeriod(period: CyclePeriodEntity): Long

    @Update
    suspend fun updatePeriod(period: CyclePeriodEntity)

    @Query("UPDATE cycle_periods SET endDate = :endDate WHERE id = :id")
    suspend fun endPeriodById(id: Long, endDate: String)

    @Query("SELECT * FROM cycle_day_logs WHERE date = :date LIMIT 1")
    suspend fun getLogForDate(date: String): CycleDayLogEntity?

    @Query("SELECT * FROM cycle_day_logs WHERE date >= :fromDate ORDER BY date DESC")
    fun getLogsSince(fromDate: String): Flow<List<CycleDayLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLog(log: CycleDayLogEntity)
}
