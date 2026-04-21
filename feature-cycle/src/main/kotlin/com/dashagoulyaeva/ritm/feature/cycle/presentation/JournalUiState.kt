package com.dashagoulyaeva.ritm.feature.cycle.presentation

import com.dashagoulyaeva.ritm.feature.cycle.domain.model.FlowIntensity
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.MoodLevel

data class JournalUiState(
    val date: String = "",
    val flow: FlowIntensity = FlowIntensity.NONE,
    val mood: MoodLevel = MoodLevel.UNKNOWN,
    val symptoms: Set<String> = emptySet(),
    val note: String = "",
    val isSaved: Boolean = false,
    val isLoading: Boolean = true,
)
