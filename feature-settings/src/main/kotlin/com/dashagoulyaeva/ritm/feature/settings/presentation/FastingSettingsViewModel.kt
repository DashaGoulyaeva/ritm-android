package com.dashagoulyaeva.ritm.feature.settings.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashagoulyaeva.ritm.core.common.PreferenceKeys
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingPlan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FastingSettingsUiState(
    val selectedPlan: FastingPlan = FastingPlan.PLAN_16_8,
    val isLoading: Boolean = true,
    val saved: Boolean = false,
)

@HiltViewModel
class FastingSettingsViewModel
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(FastingSettingsUiState())
        val uiState: StateFlow<FastingSettingsUiState> = _uiState.asStateFlow()

        init {
            dataStore.data
                .map { prefs ->
                    val raw = prefs[PreferenceKeys.FASTING_DEFAULT_PLAN]
                    runCatching { FastingPlan.valueOf(raw ?: "") }.getOrElse { FastingPlan.PLAN_16_8 }
                }
                .onEach { plan ->
                    _uiState.value = FastingSettingsUiState(selectedPlan = plan, isLoading = false)
                }
                .launchIn(viewModelScope)
        }

        fun selectPlan(plan: FastingPlan) {
            _uiState.value = _uiState.value.copy(selectedPlan = plan, saved = false)
        }

        fun savePlan() {
            val plan = _uiState.value.selectedPlan
            viewModelScope.launch {
                dataStore.edit { prefs ->
                    prefs[PreferenceKeys.FASTING_DEFAULT_PLAN] = plan.name
                }
                _uiState.value = _uiState.value.copy(saved = true)
            }
        }
    }
