package com.dashagoulyaeva.ritm.feature.habits.domain.usecase

import com.dashagoulyaeva.ritm.feature.habits.domain.model.HabitCheck
import com.dashagoulyaeva.ritm.feature.habits.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitHistory
    @Inject
    constructor(
        private val repository: HabitRepository,
    ) {
        operator fun invoke(habitId: Long): Flow<List<HabitCheck>> = repository.getChecksForHabit(habitId)
    }
