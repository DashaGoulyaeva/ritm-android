package com.dashagoulyaeva.ritm.feature.fasting.domain.usecase

import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingSession
import com.dashagoulyaeva.ritm.feature.fasting.domain.repository.FastingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFastingHistory
    @Inject
    constructor(
        private val repository: FastingRepository,
    ) {
        operator fun invoke(): Flow<List<FastingSession>> = repository.getAllSessions()
    }
