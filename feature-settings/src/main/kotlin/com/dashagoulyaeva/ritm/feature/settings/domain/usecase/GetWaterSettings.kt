package com.dashagoulyaeva.ritm.feature.settings.domain.usecase

import com.dashagoulyaeva.ritm.feature.settings.domain.model.WaterReminderSettings
import com.dashagoulyaeva.ritm.feature.settings.domain.repository.WaterSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWaterSettings
    @Inject
    constructor(
        private val repository: WaterSettingsRepository,
    ) {
        operator fun invoke(): Flow<WaterReminderSettings> = repository.observeSettings()
    }
