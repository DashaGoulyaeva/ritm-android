package com.dashagoulyaeva.ritm.feature.habits.domain.model

data class HabitCheck(
    val id: Long = 0,
    val habitId: Long,
    val date: String,
    val completedAt: Long = System.currentTimeMillis(),
)
