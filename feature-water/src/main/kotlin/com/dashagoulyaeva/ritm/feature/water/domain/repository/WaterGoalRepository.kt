package com.dashagoulyaeva.ritm.feature.water.domain.repository

import kotlinx.coroutines.flow.Flow

interface WaterGoalRepository {
    fun getGoal(): Flow<Int>

    suspend fun setGoal(glasses: Int)
}
