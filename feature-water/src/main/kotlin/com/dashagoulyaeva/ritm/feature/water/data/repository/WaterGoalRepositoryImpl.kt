package com.dashagoulyaeva.ritm.feature.water.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.dashagoulyaeva.ritm.core.common.PreferenceKeys
import com.dashagoulyaeva.ritm.feature.water.domain.repository.WaterGoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WaterGoalRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : WaterGoalRepository {
        override fun getGoal(): Flow<Int> =
            dataStore.data
                .map { prefs -> prefs[PreferenceKeys.WATER_DAILY_GOAL] ?: DEFAULT_WATER_GOAL }

        override suspend fun setGoal(glasses: Int) {
            dataStore.edit { prefs -> prefs[PreferenceKeys.WATER_DAILY_GOAL] = glasses }
        }

        private companion object {
            const val DEFAULT_WATER_GOAL = 8
        }
    }
