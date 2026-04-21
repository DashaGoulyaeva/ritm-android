package com.dashagoulyaeva.ritm.feature.cycle.domain.repository

import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CycleDayLog
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CyclePeriod
import kotlinx.coroutines.flow.Flow

interface CycleRepository {
    fun getAllPeriods(): Flow<List<CyclePeriod>>
    suspend fun getRecentPeriods(limit: Int): List<CyclePeriod>
    fun getActivePeriod(): Flow<CyclePeriod?>
    suspend fun getActivePeriodOnce(): CyclePeriod?
    suspend fun startPeriod(startDate: String): Long
    suspend fun endPeriod(periodId: Long, endDate: String)
    suspend fun getLogForDate(date: String): CycleDayLog?
    fun getLogsSince(fromDate: String): Flow<List<CycleDayLog>>
    suspend fun saveLog(log: CycleDayLog)
}
