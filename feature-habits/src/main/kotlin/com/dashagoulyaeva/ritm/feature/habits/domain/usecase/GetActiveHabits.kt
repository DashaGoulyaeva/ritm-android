package com.dashagoulyaeva.ritm.feature.habits.domain.usecase

import com.dashagoulyaeva.ritm.feature.habits.domain.model.Habit
import com.dashagoulyaeva.ritm.feature.habits.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActiveHabits @Inject constructor(
    private val repository: HabitRepository,
) {
    operator fun invoke(): Flow<List<Habit>> = repository.getActiveHabits()
}
