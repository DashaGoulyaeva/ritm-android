package com.dashagoulyaeva.ritm.feature.cycle.domain.usecase

import com.dashagoulyaeva.ritm.feature.cycle.domain.repository.CycleRepository
import javax.inject.Inject

class LogPeriodStart @Inject constructor(
    private val repository: CycleRepository,
) {
    suspend operator fun invoke(startDate: String): Long = repository.startPeriod(startDate)
}
