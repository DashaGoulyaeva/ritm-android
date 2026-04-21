package com.dashagoulyaeva.ritm.feature.fasting.presentation

import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingSession

data class FastingHistoryUiState(
    val sessions: List<FastingSession> = emptyList(),
    val isLoading: Boolean = true,
)
