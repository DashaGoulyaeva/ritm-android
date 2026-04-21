package com.dashagoulyaeva.ritm.feature.fasting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashagoulyaeva.ritm.feature.fasting.domain.usecase.GetFastingHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FastingHistoryViewModel @Inject constructor(
    private val getFastingHistory: GetFastingHistory,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FastingHistoryUiState())
    val uiState: StateFlow<FastingHistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getFastingHistory().collect { sessions ->
                _uiState.value = FastingHistoryUiState(sessions = sessions, isLoading = false)
            }
        }
    }
}
