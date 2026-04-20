package com.dashagoulyaeva.ritm.feature.water.domain.usecase

import com.dashagoulyaeva.ritm.feature.water.domain.repository.WaterGoalRepository
import javax.inject.Inject

class SetWaterGoal
    @Inject
    constructor(
        private val repository: WaterGoalRepository,
    ) {
        suspend operator fun invoke(glasses: Int) = repository.setGoal(glasses)
    }
