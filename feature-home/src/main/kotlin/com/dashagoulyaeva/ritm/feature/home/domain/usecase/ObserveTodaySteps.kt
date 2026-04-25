package com.dashagoulyaeva.ritm.feature.home.domain.usecase

import com.dashagoulyaeva.ritm.feature.home.domain.repository.StepsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTodaySteps
    @Inject
    constructor(
        private val repo: StepsRepository,
    ) {
        operator fun invoke(): Flow<Int> = repo.observeTodaySteps()
    }
