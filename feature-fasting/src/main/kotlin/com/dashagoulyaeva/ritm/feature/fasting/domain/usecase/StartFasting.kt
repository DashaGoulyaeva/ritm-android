package com.dashagoulyaeva.ritm.feature.fasting.domain.usecase

import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingPlan
import com.dashagoulyaeva.ritm.feature.fasting.domain.repository.FastingRepository
import javax.inject.Inject

class StartFasting
    @Inject
    constructor(
        private val repository: FastingRepository,
    ) {
        suspend operator fun invoke(
            plan: FastingPlan,
            customHours: Int = 0,
        ): Long = repository.startSession(plan, customHours)
    }
