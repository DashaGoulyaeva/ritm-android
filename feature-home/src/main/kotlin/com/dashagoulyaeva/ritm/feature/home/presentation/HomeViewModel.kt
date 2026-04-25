package com.dashagoulyaeva.ritm.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashagoulyaeva.ritm.core.database.entity.WaterType
import com.dashagoulyaeva.ritm.feature.habits.domain.usecase.CheckHabitToday
import com.dashagoulyaeva.ritm.feature.home.domain.usecase.HomeAggregator
import com.dashagoulyaeva.ritm.feature.water.domain.usecase.AddWaterEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val homeAggregator: HomeAggregator,
        private val addWaterEntry: AddWaterEntry,
        private val checkHabitToday: CheckHabitToday,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(HomeUiState())
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
        private val today = LocalDate.now().toString()

        init {
            viewModelScope.launch {
                homeAggregator().collect { todayState ->
                    _uiState.update { it.copy(todayState = todayState) }
                }
            }
        }

        fun addWater(type: WaterType) {
            viewModelScope.launch { addWaterEntry(type) }
        }

        fun toggleHabit(
            habitId: Long,
            checked: Boolean,
        ) {
            viewModelScope.launch { checkHabitToday(habitId, today, checked) }
        }

        fun showFastingSheet() {
            _uiState.update { it.copy(showFastingSheet = true) }
        }

        fun hideFastingSheet() {
            _uiState.update { it.copy(showFastingSheet = false) }
        }

        fun showWaterSheet() {
            _uiState.update { it.copy(showWaterSheet = true) }
        }

        fun hideWaterSheet() {
            _uiState.update { it.copy(showWaterSheet = false) }
        }
    }
