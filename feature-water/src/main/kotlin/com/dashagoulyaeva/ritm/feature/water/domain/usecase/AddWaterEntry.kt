package com.dashagoulyaeva.ritm.feature.water.domain.usecase

import com.dashagoulyaeva.ritm.core.database.entity.WaterType
import com.dashagoulyaeva.ritm.feature.water.domain.repository.WaterRepository
import javax.inject.Inject

class AddWaterEntry
    @Inject
    constructor(
        private val repository: WaterRepository,
    ) {
        suspend operator fun invoke(type: WaterType) = repository.addEntry(type)
    }
