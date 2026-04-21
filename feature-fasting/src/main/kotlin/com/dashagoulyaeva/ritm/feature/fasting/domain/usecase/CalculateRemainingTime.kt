package com.dashagoulyaeva.ritm.feature.fasting.domain.usecase

import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingSession
import javax.inject.Inject

class CalculateRemainingTime @Inject constructor() {
    operator fun invoke(session: FastingSession?): Long? {
        session ?: return null
        val remaining = session.plannedEndAt - System.currentTimeMillis()
        return maxOf(0L, remaining)
    }
}
