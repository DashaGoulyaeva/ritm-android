package com.dashagoulyaeva.ritm.feature.settings.domain.repository

import com.dashagoulyaeva.ritm.feature.settings.domain.model.WaterReminderSettings
import kotlinx.coroutines.flow.Flow

interface WaterSettingsRepository {
    fun observeSettings(): Flow<WaterReminderSettings>

    suspend fun setDailyGoal(goal: Int)

    suspend fun setReminderEnabled(enabled: Boolean)

    suspend fun setReminderTime(time: String)
}
