package com.dashagoulyaeva.ritm.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashagoulyaeva.ritm.feature.home.domain.model.StepRecord
import com.dashagoulyaeva.ritm.feature.home.domain.usecase.GetStepsHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StepsHistoryUiState(
    val records: List<StepRecord> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
)

@HiltViewModel
class StepsHistoryViewModel
    @Inject
    constructor(
        private val getStepsHistory: GetStepsHistory,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(StepsHistoryUiState())
        val uiState: StateFlow<StepsHistoryUiState> = _uiState.asStateFlow()

        init {
            load()
        }

        private fun load() {
            viewModelScope.launch {
                _uiState.value = StepsHistoryUiState(isLoading = true)
                runCatching { getStepsHistory(limit = 30) }
                    .onSuccess { records ->
                        _uiState.value = StepsHistoryUiState(records = records, isLoading = false)
                    }
                    .onFailure { e ->
                        _uiState.value = StepsHistoryUiState(isLoading = false, error = e.message)
                    }
            }
        }
    }
