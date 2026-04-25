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

class GetHabitStreakTest {
    // Простой фейк репозиторий
    private fun fakeRepo(checks: List<HabitCheck>): HabitRepository =
        object : HabitRepository {
            override fun getActiveHabits() = flowOf(emptyList<Habit>())

            override fun getAllHabits() = flowOf(emptyList<Habit>())

            override suspend fun getHabitById(habitId: Long): Habit? = null

            override suspend fun createHabit(habit: Habit): Long = 0L

            override suspend fun updateHabit(habit: Habit) = Unit

            override suspend fun archiveHabit(habitId: Long) = Unit

            override fun getChecksForHabit(habitId: Long): Flow<List<HabitCheck>> = flowOf(checks)

            override fun getChecksForDate(date: String) = flowOf(emptyList<HabitCheck>())

            override suspend fun checkHabit(
                habitId: Long,
                date: String,
            ) = Unit

            override suspend fun uncheckHabit(
                habitId: Long,
                date: String,
            ) = Unit

            override suspend fun isCheckedToday(
                habitId: Long,
                date: String,
            ): Boolean = false
        }

    @Test
    fun `streak is zero when no checks`() =
        runTest {
            val useCase = GetHabitStreak(fakeRepo(emptyList()))
            useCase(1L).collect { streak ->
                assertEquals(0, streak)
            }
        }

    @Test
    fun `streak counts consecutive days including today`() =
        runTest {
            val today = LocalDate.now()
            val checks =
                listOf(
                    HabitCheck(id = 1, habitId = 1, date = today.toString()),
                    HabitCheck(id = 2, habitId = 1, date = today.minusDays(1).toString()),
                    HabitCheck(id = 3, habitId = 1, date = today.minusDays(2).toString()),
                )
            val useCase = GetHabitStreak(fakeRepo(checks))
            useCase(1L).collect { streak ->
                assertEquals(3, streak)
            }
        }

    @Test
    fun `streak breaks on missing day`() =
        runTest {
            val today = LocalDate.now()
            val checks =
                listOf(
                    HabitCheck(id = 1, habitId = 1, date = today.toString()),
                    // day -1 missing → streak stops
                    HabitCheck(id = 2, habitId = 1, date = today.minusDays(2).toString()),
                )
            val useCase = GetHabitStreak(fakeRepo(checks))
            useCase(1L).collect { streak ->
                assertEquals(1, streak)
            }
        }

    @Test
    fun `streak is zero if today not checked`() =
        runTest {
            val today = LocalDate.now()
            val checks =
                listOf(
                    HabitCheck(id = 1, habitId = 1, date = today.minusDays(1).toString()),
                    HabitCheck(id = 2, habitId = 1, date = today.minusDays(2).toString()),
                )
            val useCase = GetHabitStreak(fakeRepo(checks))
            useCase(1L).collect { streak ->
                assertEquals(0, streak)
            }
        }
}
