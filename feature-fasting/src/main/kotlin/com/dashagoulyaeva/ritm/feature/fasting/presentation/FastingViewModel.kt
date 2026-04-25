package com.dashagoulyaeva.ritm.feature.fasting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingPlan
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingSession
import com.dashagoulyaeva.ritm.feature.fasting.domain.usecase.CalculateRemainingTime
import com.dashagoulyaeva.ritm.feature.fasting.domain.usecase.GetActiveSession
import com.dashagoulyaeva.ritm.feature.fasting.domain.usecase.StartFasting
import com.dashagoulyaeva.ritm.feature.fasting.domain.usecase.StopFasting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TIMER_TICK_INTERVAL_MS = 60_000L

@HiltViewModel
class FastingViewModel
    @Inject
    constructor(
        private val getActiveSession: GetActiveSession,
        private val startFasting: StartFasting,
        private val stopFasting: StopFasting,
        private val calculateRemainingTime: CalculateRemainingTime,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(FastingUiState())
        val uiState: StateFlow<FastingUiState> = _uiState.asStateFlow()

        init {
            observeSession()
            startTimerTick()
        }

        private fun observeSession() {
            viewModelScope.launch {
                getActiveSession().collect { session ->
                    updateStateWithSession(session)
                }
            }
        }

        private fun startTimerTick() {
            viewModelScope.launch {
                while (true) {
                    delay(TIMER_TICK_INTERVAL_MS)
                    updateStateWithSession(_uiState.value.activeSession)
                }
            }
        }

        private fun updateStateWithSession(session: FastingSession?) {
            val remaining = calculateRemainingTime(session)
            val progress =
                if (session != null && remaining != null) {
                    val total = session.plannedEndAt - session.startedAt
                    if (total > 0) (1f - remaining.toFloat() / total).coerceIn(0f, 1f) else 0f
                } else {
                    0f
                }
            _uiState.value =
                _uiState.value.copy(
                    activeSession = session,
                    remainingMs = remaining,
                    progressFraction = progress,
                    isLoading = false,
                )
        }

        fun selectPlan(plan: FastingPlan) {
            _uiState.value = _uiState.value.copy(selectedPlan = plan)
        }

        fun startFasting() {
            viewModelScope.launch {
                startFasting(_uiState.value.selectedPlan)
            }
        }

        fun stopFasting(cancelled: Boolean = false) {
            viewModelScope.launch {
                _uiState.value.activeSession?.let { session ->
                    stopFasting(session.id, cancelled)
                }
            }
        }
    }
