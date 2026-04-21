package com.dashagoulyaeva.ritm.feature.home.domain.model

import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingSession
import com.dashagoulyaeva.ritm.feature.habits.domain.model.Habit
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CycleDayInfo
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CyclePhase

data class TodayState(
    val waterCount: Int = 0,
    val waterGoal: Int = 8,
    val waterProgress: Float = 0f,
    val habits: List<Habit> = emptyList(),
    val habitCheckedIds: Set<Long> = emptySet(),
    val activeFastingSession: FastingSession? = null,
    val fastingRemainingMs: Long? = null,
    val cycleDayInfo: CycleDayInfo = CycleDayInfo(0, CyclePhase.UNKNOWN, false),
    val stepsToday: Int = 0, // TODO ST-04: wire ObserveTodaySteps when feature-steps ready
    val stepsGoal: Int = 10000,
    val isLoading: Boolean = true,
)
