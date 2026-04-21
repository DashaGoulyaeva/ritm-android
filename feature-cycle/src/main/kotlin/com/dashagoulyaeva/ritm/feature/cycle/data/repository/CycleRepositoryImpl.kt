package com.dashagoulyaeva.ritm.feature.cycle.data.repository

import com.dashagoulyaeva.ritm.core.database.dao.CycleDao
import com.dashagoulyaeva.ritm.core.database.entity.CycleDayLogEntity
import com.dashagoulyaeva.ritm.core.database.entity.CyclePeriodEntity
import com.dashagoulyaeva.ritm.core.database.entity.FlowIntensity as EntityFlowIntensity
import com.dashagoulyaeva.ritm.core.database.entity.MoodLevel as EntityMoodLevel
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CycleDayLog
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CyclePeriod
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.FlowIntensity
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.MoodLevel
import com.dashagoulyaeva.ritm.feature.cycle.domain.repository.CycleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CycleRepositoryImpl @Inject constructor(
    private val cycleDao: CycleDao,
) : CycleRepository {

    override fun getAllPeriods(): Flow<List<CyclePeriod>> =
        cycleDao.getAllPeriods().map { list -> list.map { it.toDomain() } }

    override suspend fun getRecentPeriods(limit: Int): List<CyclePeriod> =
        cycleDao.getRecentPeriods(limit).map { it.toDomain() }

    override fun getActivePeriod(): Flow<CyclePeriod?> =
        cycleDao.getActivePeriod().map { it?.toDomain() }

    override suspend fun getActivePeriodOnce(): CyclePeriod? =
        cycleDao.getActivePeriodOnce()?.toDomain()

    override suspend fun startPeriod(startDate: String): Long =
        cycleDao.insertPeriod(CyclePeriodEntity(startDate = startDate))

    override suspend fun endPeriod(periodId: Long, endDate: String) {
        val existing = cycleDao.getActivePeriodOnce() ?: return
        cycleDao.updatePeriod(existing.copy(endDate = endDate))
    }

    override suspend fun getLogForDate(date: String): CycleDayLog? =
        cycleDao.getLogForDate(date)?.toDomain()

    override fun getLogsSince(fromDate: String): Flow<List<CycleDayLog>> =
        cycleDao.getLogsSince(fromDate).map { list -> list.map { it.toDomain() } }

    override suspend fun saveLog(log: CycleDayLog) =
        cycleDao.upsertLog(log.toEntity())

    private fun CyclePeriodEntity.toDomain() = CyclePeriod(
        id = id,
        startDate = startDate,
        endDate = endDate,
    )

    private fun CycleDayLogEntity.toDomain() = CycleDayLog(
        id = id,
        date = date,
        flow = FlowIntensity.valueOf(flow.name),
        mood = MoodLevel.valueOf(mood.name),
        symptoms = if (symptoms.isBlank()) emptyList() else symptoms.split(","),
        note = note,
    )

    private fun CycleDayLog.toEntity() = CycleDayLogEntity(
        id = id,
        date = date,
        flow = EntityFlowIntensity.valueOf(flow.name),
        mood = EntityMoodLevel.valueOf(mood.name),
        symptoms = symptoms.joinToString(","),
        note = note,
    )
}
