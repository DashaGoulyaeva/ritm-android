package com.dashagoulyaeva.ritm.feature.home.domain.repository

import com.dashagoulyaeva.ritm.feature.home.domain.model.StepRecord
import kotlinx.coroutines.flow.Flow

interface StepsRepository {
    /** Live step count from the hardware sensor. Emits -1 if unavailable. */
    fun observeTodaySteps(): Flow<Int>

    suspend fun getStepsHistory(limit: Int = 30): List<StepRecord>

    suspend fun saveTodaySteps(stepCount: Int)
}
