package com.dashagoulyaeva.ritm.feature.home.presentation

data class StepsUiState(
    val todaySteps: Int = 0,
    val dailyGoal: Int = 10_000,
    val isAvailable: Boolean = true,
    val isLoading: Boolean = true,
    val error: String? = null,
) {
    val progress: Float
        get() = if (dailyGoal > 0) (todaySteps.toFloat() / dailyGoal).coerceIn(0f, 1f) else 0f

    val fallback: StepsFallback?
        get() =
            when {
                isLoading -> null
                error?.contains("permission", ignoreCase = true) == true -> StepsFallback.PermissionDenied
                !isAvailable -> StepsFallback.SensorUnavailable
                todaySteps == 0 -> StepsFallback.NoDataYet
                else -> null
            }
}

enum class StepsFallback {
    PermissionDenied,
    SensorUnavailable,
    NoDataYet,
}
