package com.dashagoulyaeva.ritm.feature.cycle.domain.usecase

import com.dashagoulyaeva.ritm.feature.cycle.domain.repository.CycleRepository
import javax.inject.Inject

class LogPeriodEnd
    @Inject
    constructor(
        private val repository: CycleRepository,
    ) {
        suspend operator fun invoke(
            periodId: Long,
            endDate: String,
        ) = repository.endPeriod(periodId, endDate)
    }
