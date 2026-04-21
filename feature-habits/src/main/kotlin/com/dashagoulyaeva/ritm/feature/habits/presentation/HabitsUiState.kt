package com.dashagoulyaeva.ritm.feature.habits.presentation

import com.dashagoulyaeva.ritm.feature.habits.domain.model.Habit

data class HabitsUiState(
    val habits: List<Habit> = emptyList(),
    val todayCheckedIds: Set<Long> = emptySet(),
    val isLoading: Boolean = true,
    val error: String? = null,
)
