package com.dashagoulyaeva.ritm.feature.water.domain.usecase

import com.dashagoulyaeva.ritm.feature.water.domain.model.WaterEntry
import com.dashagoulyaeva.ritm.feature.water.domain.repository.WaterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayWaterEntries
    @Inject
    constructor(
        private val repository: WaterRepository,
    ) {
        operator fun invoke(): Flow<List<WaterEntry>> = repository.getTodayEntries()
    }
