package com.dashagoulyaeva.ritm.feature.water.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashagoulyaeva.ritm.core.database.entity.WaterType
import com.dashagoulyaeva.ritm.feature.water.domain.model.WaterEntry
import com.dashagoulyaeva.ritm.feature.water.domain.usecase.AddWaterEntry
import com.dashagoulyaeva.ritm.feature.water.domain.usecase.DeleteWaterEntry
import com.dashagoulyaeva.ritm.feature.water.domain.usecase.GetTodayWaterEntries
import com.dashagoulyaeva.ritm.feature.water.domain.usecase.GetWaterGoal
import com.dashagoulyaeva.ritm.feature.water.domain.usecase.SetWaterGoal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaterViewModel
    @Inject
    constructor(
        private val getTodayWaterEntries: GetTodayWaterEntries,
        private val getWaterGoal: GetWaterGoal,
        private val addWaterEntry: AddWaterEntry,
        private val deleteWaterEntry: DeleteWaterEntry,
        private val setWaterGoal: SetWaterGoal,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(WaterUiState(isLoading = true))
        val uiState: StateFlow<WaterUiState> = _uiState.asStateFlow()

        init {
            observeWaterState()
        }

        fun addEntry(type: WaterType) {
            mutate { addWaterEntry(type) }
        }

        fun deleteEntry(entry: WaterEntry) {
            mutate { deleteWaterEntry(entry) }
        }

        fun setDailyGoal(glasses: Int) {
            mutate { setWaterGoal(glasses) }
        }

        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }

        private fun observeWaterState() {
            combine(
                getTodayWaterEntries(),
                getWaterGoal(),
            ) { todayEntries, dailyGoal ->
                WaterUiState(
                    todayEntries = todayEntries,
                    dailyGoal = dailyGoal,
                    isLoading = false,
                    errorMessage = null,
                )
            }
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Не удалось загрузить данные воды",
                        )
                    }
                }
                .onEach { state -> _uiState.value = state }
                .launchIn(viewModelScope)
        }

        private fun mutate(action: suspend () -> Unit) {
            viewModelScope.launch {
                runCatching { action() }
                    .onFailure { error ->
                        _uiState.update {
                            it.copy(errorMessage = error.message ?: "Операция не выполнена")
                        }
                    }
            }
        }
    }
