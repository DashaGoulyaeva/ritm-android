package com.dashagoulyaeva.ritm.feature.home.data

import com.dashagoulyaeva.ritm.core.database.dao.StepsDao
import com.dashagoulyaeva.ritm.core.database.entity.StepDailyEntity
import com.dashagoulyaeva.ritm.feature.home.domain.model.StepRecord
import com.dashagoulyaeva.ritm.feature.home.domain.repository.StepsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StepsRepositoryImpl @Inject constructor(
    private val sensor: StepsSensorDataSource,
    private val stepsDao: StepsDao,
) : StepsRepository {

    override fun observeTodaySteps(): Flow<Int> =
        sensor.observeSteps()
            .onEach { count -> if (count > 0) saveTodaySteps(count) }
            .flowOn(Dispatchers.IO)

    override suspend fun saveTodaySteps(stepCount: Int) {
        stepsDao.upsertSteps(
            StepDailyEntity(
                date = LocalDate.now().toString(),
                stepCount = stepCount,
                lastUpdatedAt = System.currentTimeMillis(),
            )
        )
    }

    override suspend fun getStepsHistory(limit: Int): List<StepRecord> =
        stepsDao.getRecentSteps(limit)
            .first()
            .map { entity -> StepRecord(date = entity.date, stepCount = entity.stepCount) }
}
