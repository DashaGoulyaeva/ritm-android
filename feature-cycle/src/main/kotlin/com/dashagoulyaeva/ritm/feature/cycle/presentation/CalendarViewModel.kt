package com.dashagoulyaeva.ritm.feature.cycle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashagoulyaeva.ritm.feature.cycle.domain.repository.CycleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

private const val CALENDAR_HISTORY_MONTHS = 3L

@HiltViewModel
class CalendarViewModel
    @Inject
    constructor(
        private val cycleRepository: CycleRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(CalendarUiState())
        val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

        init {
            val today = LocalDate.now()
            _uiState.value = _uiState.value.copy(year = today.year, month = today.monthValue)
            viewModelScope.launch {
                combine(
                    cycleRepository.getAllPeriods(),
                    cycleRepository.getLogsSince(today.minusMonths(CALENDAR_HISTORY_MONTHS).toString()),
                ) { periods, logs ->
                    CalendarUiState(
                        year = today.year,
                        month = today.monthValue,
                        periods = periods,
                        logs = logs,
                        isLoading = false,
                    )
                }.collect { _uiState.value = it }
            }
        }
    }
