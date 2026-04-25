package com.dashagoulyaeva.ritm.feature.cycle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashagoulyaeva.ritm.feature.cycle.domain.repository.CycleRepository
import com.dashagoulyaeva.ritm.feature.cycle.domain.usecase.GetCurrentCycleDay
import com.dashagoulyaeva.ritm.feature.cycle.domain.usecase.LogPeriodEnd
import com.dashagoulyaeva.ritm.feature.cycle.domain.usecase.LogPeriodStart
import com.dashagoulyaeva.ritm.feature.cycle.domain.usecase.PredictNextPeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CycleViewModel
    @Inject
    constructor(
        private val getCurrentCycleDay: GetCurrentCycleDay,
        private val logPeriodStart: LogPeriodStart,
        private val logPeriodEnd: LogPeriodEnd,
        private val predictNextPeriod: PredictNextPeriod,
        private val cycleRepository: CycleRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(CycleUiState())
        val uiState: StateFlow<CycleUiState> = _uiState.asStateFlow()

        init {
            viewModelScope.launch {
                combine(getCurrentCycleDay(), cycleRepository.getActivePeriod()) { dayInfo, active ->
                    CycleUiState(currentDayInfo = dayInfo, activePeriod = active, isLoading = false)
                }.collect { state ->
                    _uiState.value = state
                    loadPrediction()
                }
            }
        }

        private fun loadPrediction() {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(predictedNextPeriod = predictNextPeriod())
            }
        }

        fun startPeriod() {
            viewModelScope.launch { logPeriodStart(LocalDate.now().toString()) }
        }

        fun endPeriod() {
            viewModelScope.launch {
                val active = _uiState.value.activePeriod ?: return@launch
                logPeriodEnd(active.id, LocalDate.now().toString())
            }
        }
    }
