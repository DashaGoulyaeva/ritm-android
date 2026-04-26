package com.dashagoulyaeva.ritm.feature.settings.presentation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.dashagoulyaeva.ritm.core.common.PreferenceKeys
import com.dashagoulyaeva.ritm.feature.fasting.reminder.FastingCompletionWorker
import com.dashagoulyaeva.ritm.feature.habits.reminder.HabitReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit
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
        @ApplicationContext private val context: Context,
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
            val workManager = WorkManager.getInstance(context)
            if (enabled) {
                val now = Calendar.getInstance()
                val target = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 22)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
                }
                val delayMs = target.timeInMillis - now.timeInMillis
                val periodicRequest = PeriodicWorkRequestBuilder<HabitReminderWorker>(
                    1, TimeUnit.DAYS,
                )
                    .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
                    .build()
                workManager.enqueueUniquePeriodicWork(
                    HabitReminderWorker.WORK_NAME,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    periodicRequest,
                )
            } else {
                workManager.cancelUniqueWork(HabitReminderWorker.WORK_NAME)
            }
        }

        fun setFastingReminder(enabled: Boolean) {
            viewModelScope.launch {
                dataStore.edit { it[PreferenceKeys.REMINDER_FASTING_ENABLED] = enabled }
            }
            val workManager = WorkManager.getInstance(context)
            if (enabled) {
                val oneTimeRequest = OneTimeWorkRequestBuilder<FastingCompletionWorker>()
                    .setInitialDelay(12, TimeUnit.HOURS)
                    .build()
                workManager.enqueueUniqueWork(
                    FastingCompletionWorker.WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    oneTimeRequest,
                )
            } else {
                workManager.cancelUniqueWork(FastingCompletionWorker.WORK_NAME)
            }
        }
    }
