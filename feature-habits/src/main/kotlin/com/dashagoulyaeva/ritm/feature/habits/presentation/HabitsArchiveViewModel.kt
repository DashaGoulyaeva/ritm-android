package com.dashagoulyaeva.ritm.feature.habits.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashagoulyaeva.ritm.feature.habits.domain.model.Habit
import com.dashagoulyaeva.ritm.feature.habits.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HabitsArchiveUiState(
    val habits: List<Habit> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class HabitsArchiveViewModel
    @Inject
    constructor(
        private val repository: HabitRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(HabitsArchiveUiState())
        val uiState: StateFlow<HabitsArchiveUiState> = _uiState.asStateFlow()

        init {
            repository
                .getAllHabits()
                .map { all -> all.filter { it.isArchived } }
                .onEach { archived ->
                    _uiState.value = HabitsArchiveUiState(habits = archived, isLoading = false)
                }
                .launchIn(viewModelScope)
        }

        fun unarchive(habit: Habit) {
            viewModelScope.launch {
                repository.updateHabit(habit.copy(isArchived = false))
            }
        }
    }
