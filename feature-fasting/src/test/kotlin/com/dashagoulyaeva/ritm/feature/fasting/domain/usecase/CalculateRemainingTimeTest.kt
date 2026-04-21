package com.dashagoulyaeva.ritm.feature.fasting.domain.usecase

import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingPlan
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingSession
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculateRemainingTimeTest {

    private val useCase = CalculateRemainingTime()

    private fun session(startedAt: Long, plannedEndAt: Long) = FastingSession(
        id = 1L,
        plan = FastingPlan.PLAN_16_8,
        targetHours = 16,
        startedAt = startedAt,
        plannedEndAt = plannedEndAt,
        status = FastingStatus.ACTIVE,
    )

    @Test
    fun `returns null when session is null`() {
        assertNull(useCase(null))
    }

    @Test
    fun `returns positive remaining when end is in the future`() {
        val now = System.currentTimeMillis()
        val plannedEnd = now + 3_600_000L // 1 hour ahead
        val result = useCase(session(now - 3_600_000L, plannedEnd))
        assertTrue("remaining should be positive", result!! > 0)
        assertTrue("remaining should be ~1 hour", result <= 3_600_000L)
    }

    @Test
    fun `returns zero when end is in the past`() {
        val now = System.currentTimeMillis()
        val result = useCase(session(now - 3_600_000L, now - 1000L))
        assertEquals(0L, result)
    }

    @Test
    fun `full 16h session has correct planned duration`() {
        val startedAt = System.currentTimeMillis()
        val hours = 16
        val plannedEndAt = startedAt + hours * 3_600_000L
        val result = useCase(session(startedAt, plannedEndAt))
        // Remaining should be approximately 16 hours (tiny delta from System.currentTimeMillis calls)
        val expectedMs = hours * 3_600_000L
        assertTrue(result!! in (expectedMs - 1000L)..expectedMs)
    }
}
