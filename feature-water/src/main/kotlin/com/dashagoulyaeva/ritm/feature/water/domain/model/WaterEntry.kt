package com.dashagoulyaeva.ritm.feature.water.domain.model

import com.dashagoulyaeva.ritm.core.database.entity.WaterType

data class WaterEntry(
    val id: Long,
    val timestamp: Long,
    val type: WaterType,
)
