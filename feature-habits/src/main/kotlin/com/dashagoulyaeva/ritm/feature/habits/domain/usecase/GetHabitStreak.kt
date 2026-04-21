package com.dashagoulyaeva.ritm.feature.habits.domain.usecase

import com.dashagoulyaeva.ritm.feature.habits.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetHabitStreak @Inject constructor(
    private val repository: HabitRepository,
) {
    operator fun invoke(habitId: Long): Flow<Int> =
        repository.getChecksForHabit(habitId).map { checks ->
            val checkedDates = checks.map { it.date }.toSet()
            var streak = 0
            var current = LocalDate.now()
            while (checkedDates.contains(current.toString())) {
                streak++
                current = current.minusDays(1)
            }
            streak
        }
}
