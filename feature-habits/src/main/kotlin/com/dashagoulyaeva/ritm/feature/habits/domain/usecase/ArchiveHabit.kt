package com.dashagoulyaeva.ritm.feature.habits.domain.usecase

import com.dashagoulyaeva.ritm.feature.habits.domain.repository.HabitRepository
import javax.inject.Inject

class ArchiveHabit @Inject constructor(
    private val repository: HabitRepository,
) {
    suspend operator fun invoke(habitId: Long) = repository.archiveHabit(habitId)
}
