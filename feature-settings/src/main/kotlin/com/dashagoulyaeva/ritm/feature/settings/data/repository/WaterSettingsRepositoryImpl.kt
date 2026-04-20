package com.dashagoulyaeva.ritm.feature.settings.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.dashagoulyaeva.ritm.core.common.PreferenceKeys
import com.dashagoulyaeva.ritm.feature.settings.domain.model.WaterReminderSettings
import com.dashagoulyaeva.ritm.feature.settings.domain.repository.WaterSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WaterSettingsRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : WaterSettingsRepository {
        override fun observeSettings(): Flow<WaterReminderSettings> {
            return dataStore.data.map { prefs ->
                WaterReminderSettings(
                    dailyGoal = prefs[PreferenceKeys.WATER_DAILY_GOAL] ?: 8,
                    reminderEnabled = prefs[PreferenceKeys.REMINDER_WATER_ENABLED] ?: false,
                    reminderTime = prefs[PreferenceKeys.REMINDER_WATER_TIME] ?: "10:00",
                )
            }
        }

        override suspend fun setDailyGoal(goal: Int) {
            dataStore.edit { prefs -> prefs[PreferenceKeys.WATER_DAILY_GOAL] = goal }
        }

        override suspend fun setReminderEnabled(enabled: Boolean) {
            dataStore.edit { prefs -> prefs[PreferenceKeys.REMINDER_WATER_ENABLED] = enabled }
        }

        override suspend fun setReminderTime(time: String) {
            dataStore.edit { prefs -> prefs[PreferenceKeys.REMINDER_WATER_TIME] = time }
        }
    }
