package com.dashagoulyaeva.ritm.feature.water.domain.usecase

import com.dashagoulyaeva.ritm.feature.water.domain.repository.WaterGoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWaterGoal
    @Inject
    constructor(
        private val repository: WaterGoalRepository,
    ) {
        operator fun invoke(): Flow<Int> = repository.getGoal()
    }
