package com.dashagoulyaeva.ritm.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashagoulyaeva.ritm.core.common.WaterReminderScheduler
import com.dashagoulyaeva.ritm.feature.settings.domain.usecase.GetWaterSettings
import com.dashagoulyaeva.ritm.feature.settings.domain.usecase.SetWaterDailyGoal
import com.dashagoulyaeva.ritm.feature.settings.domain.usecase.SetWaterReminderEnabled
import com.dashagoulyaeva.ritm.feature.settings.domain.usecase.SetWaterReminderTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaterSettingsViewModel
    @Inject
    constructor(
        getWaterSettings: GetWaterSettings,
        private val setWaterDailyGoal: SetWaterDailyGoal,
        private val setWaterReminderEnabled: SetWaterReminderEnabled,
        private val setWaterReminderTime: SetWaterReminderTime,
        private val waterReminderScheduler: WaterReminderScheduler,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(WaterSettingsUiState(isLoading = true))
        val uiState: StateFlow<WaterSettingsUiState> = _uiState.asStateFlow()

        init {
            getWaterSettings()
                .onEach { settings ->
                    _uiState.value =
                        WaterSettingsUiState(
                            dailyGoal = settings.dailyGoal,
                            reminderEnabled = settings.reminderEnabled,
                            reminderTime = settings.reminderTime,
                            isLoading = false,
                            errorMessage = null,
                        )
                }
                .catch { error ->
                    _uiState.value =
                        WaterSettingsUiState(
                            isLoading = false,
                            errorMessage = error.message ?: "Не удалось загрузить настройки",
                        )
                }
                .launchIn(viewModelScope)
        }

        fun increaseGoal() {
            val updated = (_uiState.value.dailyGoal + 1).coerceAtMost(MAX_DAILY_GOAL)
            saveGoal(updated)
        }

        fun decreaseGoal() {
            val updated = (_uiState.value.dailyGoal - 1).coerceAtLeast(MIN_DAILY_GOAL)
            saveGoal(updated)
        }

        fun toggleReminder(enabled: Boolean) {
            viewModelScope.launch {
                runCatching {
                    setWaterReminderEnabled(enabled)
                    waterReminderScheduler.schedule(enabled, _uiState.value.reminderTime)
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(errorMessage = error.message ?: "Не удалось переключить напоминание")
                    }
                }
            }
        }

        fun updateReminderTime(time: String) {
            _uiState.update { it.copy(reminderTime = time, errorMessage = null) }
        }

        fun saveReminderTime() {
            val time = _uiState.value.reminderTime
            if (!isValidTime(time)) {
                _uiState.update {
                    it.copy(errorMessage = "Время должно быть в формате HH:mm")
                }
                return
            }

            viewModelScope.launch {
                runCatching {
                    setWaterReminderTime(time)
                    waterReminderScheduler.schedule(_uiState.value.reminderEnabled, time)
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(errorMessage = error.message ?: "Не удалось сохранить время")
                    }
                }
            }
        }

        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }

        private fun saveGoal(goal: Int) {
            viewModelScope.launch {
                runCatching {
                    setWaterDailyGoal(goal)
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(errorMessage = error.message ?: "Не удалось сохранить цель")
                    }
                }
            }
        }

        private fun isValidTime(time: String): Boolean {
            return Regex("^([01]\\d|2[0-3]):[0-5]\\d$").matches(time)
        }

        private companion object {
            const val MIN_DAILY_GOAL = 1
            const val MAX_DAILY_GOAL = 30
        }
    }
