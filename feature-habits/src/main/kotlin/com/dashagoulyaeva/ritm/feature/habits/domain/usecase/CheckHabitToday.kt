package com.dashagoulyaeva.ritm.feature.habits.domain.usecase

import com.dashagoulyaeva.ritm.feature.habits.domain.repository.HabitRepository
import javax.inject.Inject

class CheckHabitToday
    @Inject
    constructor(
        private val repository: HabitRepository,
    ) {
        suspend operator fun invoke(
            habitId: Long,
            date: String,
            checked: Boolean,
        ) {
            if (checked) {
                repository.checkHabit(habitId, date)
            } else {
                repository.uncheckHabit(habitId, date)
            }
        }
    }
