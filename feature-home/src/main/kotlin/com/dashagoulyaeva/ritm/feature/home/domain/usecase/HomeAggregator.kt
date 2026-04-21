package com.dashagoulyaeva.ritm.feature.home.domain.usecase

import com.dashagoulyaeva.ritm.feature.cycle.domain.usecase.GetCurrentCycleDay
import com.dashagoulyaeva.ritm.feature.fasting.domain.usecase.CalculateRemainingTime
import com.dashagoulyaeva.ritm.feature.fasting.domain.usecase.GetActiveSession
import com.dashagoulyaeva.ritm.feature.habits.domain.usecase.GetActiveHabits
import com.dashagoulyaeva.ritm.feature.habits.domain.usecase.GetChecksForDate
import com.dashagoulyaeva.ritm.feature.home.domain.model.TodayState
import com.dashagoulyaeva.ritm.feature.water.domain.usecase.GetTodayWaterEntries
import com.dashagoulyaeva.ritm.feature.water.domain.usecase.GetWaterGoal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

class HomeAggregator @Inject constructor(
    private val getTodayWaterEntries: GetTodayWaterEntries,
    private val getWaterGoal: GetWaterGoal,
    private val getActiveHabits: GetActiveHabits,
    private val getChecksForDate: GetChecksForDate,
    private val getActiveSession: GetActiveSession,
    private val calculateRemainingTime: CalculateRemainingTime,
    private val getCurrentCycleDay: GetCurrentCycleDay,
) {
    private val today = LocalDate.now().toString()

    operator fun invoke(): Flow<TodayState> {
        val waterFlow = combine(getTodayWaterEntries(), getWaterGoal()) { entries, goal -> Pair(entries, goal) }
        val habitsFlow = combine(getActiveHabits(), getChecksForDate(today)) { habits, checks -> Pair(habits, checks) }
        val fastingFlow = getActiveSession()
        val cycleFlow = getCurrentCycleDay()

        return combine(waterFlow, habitsFlow, fastingFlow, cycleFlow) { (water, goal), (habits, checks), session, cycleDay ->
            val remaining = calculateRemainingTime(session)
            TodayState(
                waterCount = water.size,
                waterGoal = goal,
                waterProgress = (water.size.toFloat() / goal.coerceAtLeast(1)).coerceIn(0f, 1f),
                habits = habits,
                habitCheckedIds = checks.map { it.habitId }.toSet(),
                activeFastingSession = session,
                fastingRemainingMs = remaining,
                cycleDayInfo = cycleDay,
                stepsToday = 0, // TODO ST-04
                isLoading = false,
            )
        }
    }
}
