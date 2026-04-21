package com.dashagoulyaeva.ritm.feature.habits.domain.repository

import com.dashagoulyaeva.ritm.feature.habits.domain.model.Habit
import com.dashagoulyaeva.ritm.feature.habits.domain.model.HabitCheck
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getActiveHabits(): Flow<List<Habit>>
    fun getAllHabits(): Flow<List<Habit>>
    suspend fun getHabitById(habitId: Long): Habit?
    suspend fun createHabit(habit: Habit): Long
    suspend fun updateHabit(habit: Habit)
    suspend fun archiveHabit(habitId: Long)
    fun getChecksForHabit(habitId: Long): Flow<List<HabitCheck>>
    fun getChecksForDate(date: String): Flow<List<HabitCheck>>
    suspend fun checkHabit(habitId: Long, date: String)
    suspend fun uncheckHabit(habitId: Long, date: String)
    suspend fun isCheckedToday(habitId: Long, date: String): Boolean
}
