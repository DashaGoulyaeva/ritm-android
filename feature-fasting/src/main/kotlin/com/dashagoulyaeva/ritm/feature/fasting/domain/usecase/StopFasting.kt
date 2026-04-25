package com.dashagoulyaeva.ritm.feature.fasting.domain.usecase

import com.dashagoulyaeva.ritm.feature.fasting.domain.repository.FastingRepository
import javax.inject.Inject

class StopFasting
    @Inject
    constructor(
        private val repository: FastingRepository,
    ) {
        suspend operator fun invoke(
            sessionId: Long,
            cancelled: Boolean = false,
        ) = repository.stopSession(sessionId, cancelled)
    }
