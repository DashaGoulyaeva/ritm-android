package com.dashagoulyaeva.ritm.feature.habits.domain.usecase

import com.dashagoulyaeva.ritm.feature.habits.domain.model.HabitCheck
import com.dashagoulyaeva.ritm.feature.habits.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChecksForDate
    @Inject
    constructor(
        private val repository: HabitRepository,
    ) {
        operator fun invoke(date: String): Flow<List<HabitCheck>> = repository.getChecksForDate(date)
    }
