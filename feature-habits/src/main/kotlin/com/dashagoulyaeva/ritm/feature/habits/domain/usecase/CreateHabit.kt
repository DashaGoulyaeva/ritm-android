package com.dashagoulyaeva.ritm.feature.habits.domain.usecase

import com.dashagoulyaeva.ritm.feature.habits.domain.model.Habit
import com.dashagoulyaeva.ritm.feature.habits.domain.repository.HabitRepository
import javax.inject.Inject

class CreateHabit
    @Inject
    constructor(
        private val repository: HabitRepository,
    ) {
        suspend operator fun invoke(habit: Habit): Long = repository.createHabit(habit)
    }
