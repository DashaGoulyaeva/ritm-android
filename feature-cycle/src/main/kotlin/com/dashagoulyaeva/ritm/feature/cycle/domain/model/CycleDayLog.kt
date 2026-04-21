package com.dashagoulyaeva.ritm.feature.cycle.domain.model

enum class FlowIntensity { NONE, LIGHT, MEDIUM, HEAVY }
enum class MoodLevel { UNKNOWN, GREAT, GOOD, NEUTRAL, LOW, AWFUL }

data class CycleDayLog(
    val id: Long = 0,
    val date: String,
    val flow: FlowIntensity = FlowIntensity.NONE,
    val mood: MoodLevel = MoodLevel.UNKNOWN,
    val symptoms: List<String> = emptyList(),
    val note: String = "",
)
