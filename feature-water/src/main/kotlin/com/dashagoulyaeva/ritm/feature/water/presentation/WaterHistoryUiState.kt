package com.dashagoulyaeva.ritm.feature.water.presentation

import com.dashagoulyaeva.ritm.feature.water.domain.model.WaterEntry
import java.time.LocalDate

data class WaterHistoryDayGroup(
    val date: LocalDate,
    val entries: List<WaterEntry>,
)

data class WaterHistoryUiState(
    val days: List<WaterHistoryDayGroup> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
