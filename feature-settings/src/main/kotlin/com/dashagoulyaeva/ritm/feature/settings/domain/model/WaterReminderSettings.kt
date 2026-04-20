package com.dashagoulyaeva.ritm.feature.settings.domain.model

data class WaterReminderSettings(
    val dailyGoal: Int,
    val reminderEnabled: Boolean,
    val reminderTime: String,
)
