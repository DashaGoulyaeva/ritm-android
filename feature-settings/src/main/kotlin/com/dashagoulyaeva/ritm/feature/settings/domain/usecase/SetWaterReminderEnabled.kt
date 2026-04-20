package com.dashagoulyaeva.ritm.feature.settings.domain.usecase

import com.dashagoulyaeva.ritm.feature.settings.domain.repository.WaterSettingsRepository
import javax.inject.Inject

class SetWaterReminderEnabled
    @Inject
    constructor(
        private val repository: WaterSettingsRepository,
    ) {
        suspend operator fun invoke(enabled: Boolean) = repository.setReminderEnabled(enabled)
    }
