package com.dashagoulyaeva.ritm.feature.home.presentation

import com.dashagoulyaeva.ritm.feature.home.domain.model.TodayState

data class HomeUiState(
    val todayState: TodayState = TodayState(),
    val showFastingSheet: Boolean = false,
    val showWaterSheet: Boolean = false,
)
