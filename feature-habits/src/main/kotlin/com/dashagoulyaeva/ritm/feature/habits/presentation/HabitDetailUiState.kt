package com.dashagoulyaeva.ritm.feature.habits.presentation

import com.dashagoulyaeva.ritm.feature.habits.domain.model.Habit
import com.dashagoulyaeva.ritm.feature.habits.domain.model.HabitCheck

data class HabitDetailUiState(
    val habit: Habit? = null,
    val checks: List<HabitCheck> = emptyList(),
    val currentStreak: Int = 0,
    val isLoading: Boolean = true,
)
