package com.dashagoulyaeva.ritm.feature.habits.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashagoulyaeva.ritm.feature.habits.domain.model.Habit
import com.dashagoulyaeva.ritm.feature.habits.domain.usecase.ArchiveHabit
import com.dashagoulyaeva.ritm.feature.habits.domain.usecase.CheckHabitToday
import com.dashagoulyaeva.ritm.feature.habits.domain.usecase.CreateHabit
import com.dashagoulyaeva.ritm.feature.habits.domain.usecase.GetActiveHabits
import com.dashagoulyaeva.ritm.feature.habits.domain.usecase.GetChecksForDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel
    @Inject
    constructor(
        private val getActiveHabits: GetActiveHabits,
        private val getChecksForDate: GetChecksForDate,
        private val checkHabitToday: CheckHabitToday,
        private val createHabit: CreateHabit,
        private val archiveHabit: ArchiveHabit,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(HabitsUiState())
        val uiState: StateFlow<HabitsUiState> = _uiState.asStateFlow()
        val today: String = LocalDate.now().toString()

        init {
            viewModelScope.launch {
                combine(getActiveHabits(), getChecksForDate(today)) { habits, checks ->
                    HabitsUiState(
                        habits = habits,
                        todayCheckedIds = checks.map { it.habitId }.toSet(),
                        isLoading = false,
                    )
                }.collect { _uiState.value = it }
            }
        }

        fun toggleHabit(
            habitId: Long,
            checked: Boolean,
        ) {
            viewModelScope.launch { checkHabitToday(habitId, today, checked) }
        }

        fun createNewHabit(
            title: String,
            emoji: String = "✅",
        ) {
            viewModelScope.launch {
                createHabit(Habit(title = title, iconEmoji = emoji))
            }
        }

        fun archiveHabit(habitId: Long) {
            viewModelScope.launch { archiveHabit.invoke(habitId) }
        }
    }
