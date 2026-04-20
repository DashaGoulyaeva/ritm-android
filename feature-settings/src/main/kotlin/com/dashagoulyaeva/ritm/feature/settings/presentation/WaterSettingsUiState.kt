package com.dashagoulyaeva.ritm.feature.settings.presentation

data class WaterSettingsUiState(
    val dailyGoal: Int = 8,
    val reminderEnabled: Boolean = false,
    val reminderTime: String = "10:00",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
