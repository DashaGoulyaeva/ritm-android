package com.dashagoulyaeva.ritm.feature.settings.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashagoulyaeva.ritm.core.common.PreferenceKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReminderSettingsUiState(
    val habitsReminderEnabled: Boolean = false,
    val fastingReminderEnabled: Boolean = false,
)

@HiltViewModel
class ReminderSettingsViewModel
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ReminderSettingsUiState())
        val uiState: StateFlow<ReminderSettingsUiState> = _uiState.asStateFlow()

        init {
            dataStore.data
                .map { prefs ->
                    ReminderSettingsUiState(
                        habitsReminderEnabled = prefs[PreferenceKeys.REMINDER_HABITS_ENABLED] ?: false,
                        fastingReminderEnabled = prefs[PreferenceKeys.REMINDER_FASTING_ENABLED] ?: false,
                    )
                }
                .onEach { _uiState.value = it }
                .launchIn(viewModelScope)
        }

        fun setHabitsReminder(enabled: Boolean) {
            viewModelScope.launch {
                dataStore.edit { it[PreferenceKeys.REMINDER_HABITS_ENABLED] = enabled }
            }
        }

        fun setFastingReminder(enabled: Boolean) {
            viewModelScope.launch {
                dataStore.edit { it[PreferenceKeys.REMINDER_FASTING_ENABLED] = enabled }
            }
        }
    }
