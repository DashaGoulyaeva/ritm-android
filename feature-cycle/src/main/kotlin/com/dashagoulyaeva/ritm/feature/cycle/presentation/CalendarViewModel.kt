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

@HiltViewModel
class CalendarViewModel @Inject constructor(
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
                cycleRepository.getLogsSince(today.minusMonths(3).toString()),
            ) { periods, logs ->
                CalendarUiState(year = today.year, month = today.monthValue, periods = periods, logs = logs, isLoading = false)
            }.collect { _uiState.value = it }
        }
    }
}
