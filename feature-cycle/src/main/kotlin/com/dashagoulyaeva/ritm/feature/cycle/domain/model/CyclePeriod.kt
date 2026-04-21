package com.dashagoulyaeva.ritm.feature.cycle.domain.model

data class CyclePeriod(
    val id: Long = 0,
    val startDate: String, // "yyyy-MM-dd"
    val endDate: String? = null,
)
