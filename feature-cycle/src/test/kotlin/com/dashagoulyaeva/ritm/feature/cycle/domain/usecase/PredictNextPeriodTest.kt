package com.dashagoulyaeva.ritm.feature.cycle.domain.usecase

import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CycleDayLog
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CyclePeriod
import com.dashagoulyaeva.ritm.feature.cycle.domain.repository.CycleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class PredictNextPeriodTest {

    private fun fakeRepo(periods: List<CyclePeriod>): CycleRepository = object : CycleRepository {
        override fun getAllPeriods(): Flow<List<CyclePeriod>> = flowOf(periods)
        override suspend fun getRecentPeriods(limit: Int): List<CyclePeriod> =
            periods.filter { it.endDate != null }.sortedBy { it.startDate }.takeLast(limit)
        override fun getActivePeriod(): Flow<CyclePeriod?> = flowOf(null)
        override suspend fun getActivePeriodOnce(): CyclePeriod? = null
        override suspend fun startPeriod(startDate: String): Long = 0L
        override suspend fun endPeriod(periodId: Long, endDate: String) = Unit
        override suspend fun getLogForDate(date: String): CycleDayLog? = null
        override fun getLogsSince(fromDate: String): Flow<List<CycleDayLog>> = flowOf(emptyList())
        override suspend fun saveLog(log: CycleDayLog) = Unit
    }

    @Test
    fun `returns null when fewer than 2 completed periods`() = runTest {
        val repo = fakeRepo(listOf(
            CyclePeriod(1, "2026-01-01", null), // active, no endDate
        ))
        assertNull(PredictNextPeriod(repo)())
    }

    @Test
    fun `returns null when only 1 completed period`() = runTest {
        val repo = fakeRepo(listOf(
            CyclePeriod(1, "2026-01-01", "2026-01-05"),
        ))
        assertNull(PredictNextPeriod(repo)())
    }

    @Test
    fun `predicts next period with 2 completed cycles of 28 days`() = runTest {
        val repo = fakeRepo(listOf(
            CyclePeriod(1, "2026-01-01", "2026-01-05"),
            CyclePeriod(2, "2026-01-29", "2026-02-02"), // 28 days later
        ))
        val result = PredictNextPeriod(repo)()
        // Expected: 2026-01-29 + 28 days = 2026-02-26
        assertEquals("2026-02-26", result)
    }

    @Test
    fun `predicts with average of multiple cycle lengths`() = runTest {
        val repo = fakeRepo(listOf(
            CyclePeriod(1, "2026-01-01", "2026-01-05"), // cycle 1 start
            CyclePeriod(2, "2026-01-29", "2026-02-02"), // 28 days (cycle 1->2)
            CyclePeriod(3, "2026-02-28", "2026-03-03"), // 30 days (cycle 2->3)
        ))
        val result = PredictNextPeriod(repo)()
        // avg = (28+30)/2 = 29, last start = 2026-02-28, prediction = 2026-03-29
        assertEquals("2026-03-29", result)
    }
}
