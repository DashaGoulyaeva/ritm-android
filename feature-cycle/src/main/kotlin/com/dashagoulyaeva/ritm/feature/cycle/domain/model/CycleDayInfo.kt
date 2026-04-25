package com.dashagoulyaeva.ritm.feature.cycle.domain.model

data class CycleDayInfo(
    val dayOfCycle: Int,
    val phase: CyclePhase,
    val isInPeriod: Boolean,
)
