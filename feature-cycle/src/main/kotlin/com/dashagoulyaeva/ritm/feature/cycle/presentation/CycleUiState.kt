package com.dashagoulyaeva.ritm.feature.cycle.presentation

import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CycleDayInfo
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CyclePeriod
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CyclePhase

data class CycleUiState(
    val currentDayInfo: CycleDayInfo = CycleDayInfo(0, CyclePhase.UNKNOWN, false),
    val activePeriod: CyclePeriod? = null,
    val predictedNextPeriod: String? = null,
    val isLoading: Boolean = true,
)
