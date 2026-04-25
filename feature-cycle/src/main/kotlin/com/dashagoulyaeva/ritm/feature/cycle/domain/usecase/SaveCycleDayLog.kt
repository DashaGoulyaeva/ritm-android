package com.dashagoulyaeva.ritm.feature.cycle.domain.usecase

import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CycleDayLog
import com.dashagoulyaeva.ritm.feature.cycle.domain.repository.CycleRepository
import javax.inject.Inject

class SaveCycleDayLog
    @Inject
    constructor(
        private val repository: CycleRepository,
    ) {
        suspend operator fun invoke(log: CycleDayLog) = repository.saveLog(log)
    }
