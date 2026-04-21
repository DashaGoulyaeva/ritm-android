package com.dashagoulyaeva.ritm.feature.habits.domain.model

data class Habit(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val iconEmoji: String = "✅",
    val colorHex: String = "#66BB6A",
    val targetDays: String = "1111111",
    val reminderTime: String? = null,
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
)
