package com.dashagoulyaeva.ritm.feature.water.presentation

import com.dashagoulyaeva.ritm.feature.water.domain.model.WaterEntry

data class WaterUiState(
    val todayEntries: List<WaterEntry> = emptyList(),
    val dailyGoal: Int = 8,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val todayCount: Int get() = todayEntries.size
    val progress: Float get() = if (dailyGoal > 0) todayCount.toFloat() / dailyGoal else 0f
    val isGoalReached: Boolean get() = todayCount >= dailyGoal
}
