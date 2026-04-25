package com.dashagoulyaeva.ritm.feature.fasting.presentation

import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingPlan
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingSession

data class FastingUiState(
    val activeSession: FastingSession? = null,
    val remainingMs: Long? = null,
    val progressFraction: Float = 0f,
    val selectedPlan: FastingPlan = FastingPlan.PLAN_16_8,
    val isLoading: Boolean = true,
)
