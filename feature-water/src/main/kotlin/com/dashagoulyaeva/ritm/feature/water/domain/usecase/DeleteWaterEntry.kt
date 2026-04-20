package com.dashagoulyaeva.ritm.feature.water.domain.usecase

import com.dashagoulyaeva.ritm.feature.water.domain.model.WaterEntry
import com.dashagoulyaeva.ritm.feature.water.domain.repository.WaterRepository
import javax.inject.Inject

class DeleteWaterEntry
    @Inject
    constructor(
        private val repository: WaterRepository,
    ) {
        suspend operator fun invoke(entry: WaterEntry) = repository.deleteEntry(entry)
    }
