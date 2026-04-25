package com.dashagoulyaeva.ritm.feature.habits.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashagoulyaeva.ritm.feature.habits.domain.repository.HabitRepository
import com.dashagoulyaeva.ritm.feature.habits.domain.usecase.ArchiveHabit
import com.dashagoulyaeva.ritm.feature.habits.domain.usecase.GetHabitHistory
import com.dashagoulyaeva.ritm.feature.habits.domain.usecase.GetHabitStreak
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val habitRepository: HabitRepository,
        private val getHabitHistory: GetHabitHistory,
        private val getHabitStreak: GetHabitStreak,
        private val archiveHabit: ArchiveHabit,
    ) : ViewModel() {
        private val habitId: Long = checkNotNull(savedStateHandle["habitId"])

        private val _uiState = MutableStateFlow(HabitDetailUiState())
        val uiState: StateFlow<HabitDetailUiState> = _uiState.asStateFlow()

        init {
            observeHabitDetail()
        }

        private fun observeHabitDetail() {
            viewModelScope.launch {
                combine(
                    getHabitHistory(habitId),
                    getHabitStreak(habitId),
                ) { checks, streak ->
                    val habit = habitRepository.getHabitById(habitId)
                    HabitDetailUiState(
                        habit = habit,
                        checks = checks.sortedByDescending { it.date },
                        currentStreak = streak,
                        isLoading = false,
                    )
                }.collect { _uiState.value = it }
            }
        }

        fun archive() {
            viewModelScope.launch {
                archiveHabit(habitId)
            }
        }
    }
