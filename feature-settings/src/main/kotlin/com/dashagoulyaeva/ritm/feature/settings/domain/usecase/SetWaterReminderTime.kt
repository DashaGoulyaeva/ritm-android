package com.dashagoulyaeva.ritm.feature.settings.domain.usecase

import com.dashagoulyaeva.ritm.feature.settings.domain.repository.WaterSettingsRepository
import javax.inject.Inject

class SetWaterReminderTime
    @Inject
    constructor(
        private val repository: WaterSettingsRepository,
    ) {
        suspend operator fun invoke(time: String) = repository.setReminderTime(time)
    }
