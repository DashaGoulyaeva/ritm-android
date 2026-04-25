package com.dashagoulyaeva.ritm.feature.cycle.domain.model

data class CyclePeriod(
    val id: Long = 0,
    val startDate: String,
    val endDate: String? = null,
)
