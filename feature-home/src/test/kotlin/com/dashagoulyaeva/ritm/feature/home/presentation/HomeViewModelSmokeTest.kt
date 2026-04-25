package com.dashagoulyaeva.ritm.feature.home.presentation

import com.dashagoulyaeva.ritm.core.database.entity.WaterType
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CycleDayLog
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CyclePeriod
import com.dashagoulyaeva.ritm.feature.cycle.domain.repository.CycleRepository
import com.dashagoulyaeva.ritm.feature.cycle.domain.usecase.GetCurrentCycleDay
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingPlan
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingSession
import com.dashagoulyaeva.ritm.feature.fasting.domain.repository.FastingRepository
import com.dashagoulyaeva.ritm.feature.fasting.domain.usecase.CalculateRemainingTime
import com.dashagoulyaeva.ritm.feature.fasting.domain.usecase.GetActiveSession
import com.dashagoulyaeva.ritm.feature.habits.domain.model.Habit
import com.dashagoulyaeva.ritm.feature.habits.domain.model.HabitCheck
import com.dashagoulyaeva.ritm.feature.habits.domain.repository.HabitRepository
import com.dashagoulyaeva.ritm.feature.habits.domain.usecase.CheckHabitToday
import com.dashagoulyaeva.ritm.feature.habits.domain.usecase.GetActiveHabits
import com.dashagoulyaeva.ritm.feature.habits.domain.usecase.GetChecksForDate
import com.dashagoulyaeva.ritm.feature.home.domain.usecase.HomeAggregator
import com.dashagoulyaeva.ritm.feature.water.domain.model.WaterEntry
import com.dashagoulyaeva.ritm.feature.water.domain.repository.WaterGoalRepository
import com.dashagoulyaeva.ritm.feature.water.domain.repository.WaterRepository
import com.dashagoulyaeva.ritm.feature.water.domain.usecase.AddWaterEntry
import com.dashagoulyaeva.ritm.feature.water.domain.usecase.GetTodayWaterEntries
import com.dashagoulyaeva.ritm.feature.water.domain.usecase.GetWaterGoal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelSmokeTest {
    // ── Fake repositories ──────────────────────────────────────────────────

    private class FakeWaterRepository : WaterRepository {
        override fun getTodayEntries(): Flow<List<WaterEntry>> = flowOf(emptyList())

        override fun getAllEntries(): Flow<List<WaterEntry>> = flowOf(emptyList())

        override suspend fun addEntry(type: WaterType) = Unit

        override suspend fun deleteEntry(entry: WaterEntry) = Unit
    }

    private class FakeWaterGoalRepository : WaterGoalRepository {
        override fun getGoal(): Flow<Int> = flowOf(8)

        override suspend fun setGoal(glasses: Int) = Unit
    }

    private class FakeHabitRepository : HabitRepository {
        override fun getActiveHabits(): Flow<List<Habit>> = flowOf(emptyList())

        override fun getAllHabits(): Flow<List<Habit>> = flowOf(emptyList())

        override suspend fun getHabitById(habitId: Long): Habit? = null

        override suspend fun createHabit(habit: Habit): Long = 0L

        override suspend fun updateHabit(habit: Habit) = Unit

        override suspend fun archiveHabit(habitId: Long) = Unit

        override fun getChecksForHabit(habitId: Long): Flow<List<HabitCheck>> = flowOf(emptyList())

        override fun getChecksForDate(date: String): Flow<List<HabitCheck>> = flowOf(emptyList())

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

    private class FakeFastingRepository : FastingRepository {
        override fun getActiveSession(): Flow<FastingSession?> = flowOf(null)

        override suspend fun getActiveSessionOnce(): FastingSession? = null

        override fun getAllSessions(): Flow<List<FastingSession>> = flowOf(emptyList())

        override fun getSessionsSince(fromMs: Long): Flow<List<FastingSession>> = flowOf(emptyList())

        override suspend fun startSession(
            plan: FastingPlan,
            customHours: Int,
        ): Long = 0L

        override suspend fun stopSession(
            sessionId: Long,
            cancelled: Boolean,
        ) = Unit
    }

    private class FakeCycleRepository : CycleRepository {
        override fun getAllPeriods(): Flow<List<CyclePeriod>> = flowOf(emptyList())

        override suspend fun getRecentPeriods(limit: Int): List<CyclePeriod> = emptyList()

        override fun getActivePeriod(): Flow<CyclePeriod?> = flowOf(null)

        override suspend fun getActivePeriodOnce(): CyclePeriod? = null

        override suspend fun startPeriod(startDate: String): Long = 0L

        override suspend fun endPeriod(
            periodId: Long,
            endDate: String,
        ) = Unit

        override suspend fun getLogForDate(date: String): CycleDayLog? = null

        override fun getLogsSince(fromDate: String): Flow<List<CycleDayLog>> = flowOf(emptyList())

        override suspend fun saveLog(log: CycleDayLog) = Unit
    }

    // ── Test setup ─────────────────────────────────────────────────────────

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val waterRepo = FakeWaterRepository()
        val waterGoalRepo = FakeWaterGoalRepository()
        val habitRepo = FakeHabitRepository()
        val fastingRepo = FakeFastingRepository()
        val cycleRepo = FakeCycleRepository()

        val aggregator =
            HomeAggregator(
                getTodayWaterEntries = GetTodayWaterEntries(waterRepo),
                getWaterGoal = GetWaterGoal(waterGoalRepo),
                getActiveHabits = GetActiveHabits(habitRepo),
                getChecksForDate = GetChecksForDate(habitRepo),
                getActiveSession = GetActiveSession(fastingRepo),
                calculateRemainingTime = CalculateRemainingTime(),
                getCurrentCycleDay = GetCurrentCycleDay(cycleRepo),
            )

        viewModel =
            HomeViewModel(
                homeAggregator = aggregator,
                addWaterEntry = AddWaterEntry(waterRepo),
                checkHabitToday = CheckHabitToday(habitRepo),
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── Tests ──────────────────────────────────────────────────────────────

    @Test
    fun `initial state is not null`() =
        runTest {
            assertNotNull(viewModel.uiState.value)
        }

    @Test
    fun `initial isLoading is true before aggregator emits`() =
        runTest {
            // The aggregator coroutine is launched in init but has not yet run on
            // testDispatcher (no advanceUntilIdle call), so todayState.isLoading
            // should still be the default value (true) from TodayState().
            assertTrue(viewModel.uiState.value.todayState.isLoading)
        }

    @Test
    fun `showWaterSheet toggles correctly`() =
        runTest {
            assertFalse(viewModel.uiState.value.showWaterSheet)

            viewModel.showWaterSheet()

            assertTrue(viewModel.uiState.value.showWaterSheet)
        }

    @Test
    fun `hideWaterSheet resets flag`() =
        runTest {
            viewModel.showWaterSheet()
            viewModel.hideWaterSheet()

            assertFalse(viewModel.uiState.value.showWaterSheet)
        }

    @Test
    fun `showFastingSheet toggles correctly`() =
        runTest {
            assertFalse(viewModel.uiState.value.showFastingSheet)

            viewModel.showFastingSheet()

            assertTrue(viewModel.uiState.value.showFastingSheet)
        }
}
