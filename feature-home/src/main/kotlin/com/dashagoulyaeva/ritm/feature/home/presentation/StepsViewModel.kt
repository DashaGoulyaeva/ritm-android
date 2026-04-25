package com.dashagoulyaeva.ritm.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashagoulyaeva.ritm.feature.home.domain.usecase.ObserveTodaySteps
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StepsViewModel
    @Inject
    constructor(
        private val observeTodaySteps: ObserveTodaySteps,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(StepsUiState())
        val uiState: StateFlow<StepsUiState> = _uiState.asStateFlow()

        init {
            viewModelScope.launch {
                observeTodaySteps().collect { steps ->
                    _uiState.update { state ->
                        state.copy(
                            todaySteps = if (steps >= 0) steps else 0,
                            isAvailable = steps >= 0,
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }
