package com.dashagoulyaeva.ritm.feature.cycle.presentation

import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CycleDayLog
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CyclePeriod

data class CalendarUiState(
    val year: Int = 0,
    val month: Int = 0,
    val periods: List<CyclePeriod> = emptyList(),
    val logs: List<CycleDayLog> = emptyList(),
    val isLoading: Boolean = true,
)
