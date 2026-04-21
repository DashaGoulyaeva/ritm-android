package com.dashagoulyaeva.ritm.feature.fasting.data.repository

import com.dashagoulyaeva.ritm.core.database.dao.FastingDao
import com.dashagoulyaeva.ritm.core.database.entity.FastingSessionEntity
import com.dashagoulyaeva.ritm.core.database.entity.FastingPlan as EntityFastingPlan
import com.dashagoulyaeva.ritm.core.database.entity.FastingStatus as EntityFastingStatus
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingPlan
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingSession
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingStatus
import com.dashagoulyaeva.ritm.feature.fasting.domain.repository.FastingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FastingRepositoryImpl @Inject constructor(
    private val fastingDao: FastingDao,
) : FastingRepository {

    override fun getActiveSession(): Flow<FastingSession?> =
        fastingDao.getActiveSession().map { it?.toDomain() }

    override suspend fun getActiveSessionOnce(): FastingSession? =
        fastingDao.getActiveSessionOnce()?.toDomain()

    override fun getAllSessions(): Flow<List<FastingSession>> =
        fastingDao.getAllSessions().map { list -> list.map { it.toDomain() } }

    override fun getSessionsSince(fromMs: Long): Flow<List<FastingSession>> =
        fastingDao.getSessionsSince(fromMs).map { list -> list.map { it.toDomain() } }

    override suspend fun startSession(plan: FastingPlan, customHours: Int): Long {
        val hours = if (plan == FastingPlan.CUSTOM) customHours else plan.hours
        val now = System.currentTimeMillis()
        val entity = FastingSessionEntity(
            plan = EntityFastingPlan.valueOf(plan.name),
            targetHours = hours,
            startedAt = now,
            plannedEndAt = now + hours * 60 * 60 * 1000L,
        )
        return fastingDao.insertSession(entity)
    }

    override suspend fun stopSession(sessionId: Long, cancelled: Boolean) {
        val status = if (cancelled) EntityFastingStatus.CANCELLED else EntityFastingStatus.COMPLETED
        fastingDao.endSession(sessionId, status, System.currentTimeMillis())
    }

    private fun FastingSessionEntity.toDomain() = FastingSession(
        id = id,
        plan = FastingPlan.valueOf(plan.name),
        targetHours = targetHours,
        startedAt = startedAt,
        plannedEndAt = plannedEndAt,
        actualEndAt = actualEndAt,
        status = FastingStatus.valueOf(status.name),
    )
}
