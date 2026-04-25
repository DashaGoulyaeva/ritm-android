package com.dashagoulyaeva.ritm.feature.habits.domain.usecase

import com.dashagoulyaeva.ritm.feature.habits.domain.model.Habit
import com.dashagoulyaeva.ritm.feature.habits.domain.model.HabitCheck
import com.dashagoulyaeva.ritm.feature.habits.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class CheckHabitTodayTest {
    private class SpyRepository : HabitRepository {
        val checkedCalls = mutableListOf<Pair<Long, String>>()

        val uncheckedCalls = mutableListOf<Pair<Long, String>>()

        override fun getActiveHabits() = flowOf(emptyList<Habit>())

        override fun getAllHabits() = flowOf(emptyList<Habit>())

        override suspend fun getHabitById(habitId: Long): Habit? = null

        override suspend fun createHabit(habit: Habit): Long = 0L

        override suspend fun updateHabit(habit: Habit) = Unit

        override suspend fun archiveHabit(habitId: Long) = Unit

        override fun getChecksForHabit(habitId: Long): Flow<List<HabitCheck>> = flowOf(emptyList())

        override fun getChecksForDate(date: String): Flow<List<HabitCheck>> = flowOf(emptyList())

        override suspend fun checkHabit(
            habitId: Long,
            date: String,
        ) {
            checkedCalls += Pair(habitId, date)
        }

        override suspend fun uncheckHabit(
            habitId: Long,
            date: String,
        ) {
            uncheckedCalls += Pair(habitId, date)
        }

        override suspend fun isCheckedToday(
            habitId: Long,
            date: String,
        ): Boolean = false
    }

    @Test
    fun `checked true calls checkHabit`() =
        runTest {
            val spy = SpyRepository()
            val useCase = CheckHabitToday(spy)
            val today = LocalDate.now().toString()

            useCase(habitId = 42L, date = today, checked = true)

            assertEquals(1, spy.checkedCalls.size)
            assertEquals(0, spy.uncheckedCalls.size)
            assertEquals(42L, spy.checkedCalls[0].first)
            assertEquals(today, spy.checkedCalls[0].second)
        }

    @Test
    fun `checked false calls uncheckHabit`() =
        runTest {
            val spy = SpyRepository()
            val useCase = CheckHabitToday(spy)
            val today = LocalDate.now().toString()

            useCase(habitId = 7L, date = today, checked = false)

            assertEquals(0, spy.checkedCalls.size)
            assertEquals(1, spy.uncheckedCalls.size)
            assertEquals(7L, spy.uncheckedCalls[0].first)
            assertEquals(today, spy.uncheckedCalls[0].second)
        }

    @Test
    fun `multiple calls accumulate correctly`() =
        runTest {
            val spy = SpyRepository()
            val useCase = CheckHabitToday(spy)
            val today = LocalDate.now().toString()
            val yesterday = LocalDate.now().minusDays(1).toString()

            useCase(habitId = 1L, date = today, checked = true)
            useCase(habitId = 1L, date = yesterday, checked = false)
            useCase(habitId = 2L, date = today, checked = true)

            assertEquals(2, spy.checkedCalls.size)
            assertEquals(1, spy.uncheckedCalls.size)
        }
}
