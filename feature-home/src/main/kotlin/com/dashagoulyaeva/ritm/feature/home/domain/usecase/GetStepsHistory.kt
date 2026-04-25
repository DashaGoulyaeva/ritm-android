package com.dashagoulyaeva.ritm.feature.home.domain.usecase

import com.dashagoulyaeva.ritm.feature.home.domain.model.StepRecord
import com.dashagoulyaeva.ritm.feature.home.domain.repository.StepsRepository
import javax.inject.Inject

class GetStepsHistory
    @Inject
    constructor(
        private val repo: StepsRepository,
    ) {
        suspend operator fun invoke(limit: Int = 30): List<StepRecord> = repo.getStepsHistory(limit)
    }
