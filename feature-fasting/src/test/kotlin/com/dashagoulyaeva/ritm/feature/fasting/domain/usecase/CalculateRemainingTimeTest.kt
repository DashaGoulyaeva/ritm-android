package com.dashagoulyaeva.ritm.feature.fasting.domain.usecase

import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingPlan
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingSession
import com.dashagoulyaeva.ritm.feature.fasting.domain.model.FastingStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

private const val HOUR_IN_MILLIS = 3_600_000L
private const val SECOND_IN_MILLIS = 1000L
private const val SIXTEEN_HOURS = 16

class CalculateRemainingTimeTest {
    private val useCase = CalculateRemainingTime()

    private fun session(
        startedAt: Long,
        plannedEndAt: Long,
    ): FastingSession =
        FastingSession(
            id = 1L,
            plan = FastingPlan.PLAN_16_8,
            targetHours = SIXTEEN_HOURS,
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
        val plannedEnd = now + HOUR_IN_MILLIS
        val result = useCase(session(now - HOUR_IN_MILLIS, plannedEnd))
        assertTrue("remaining should be positive", result!! > 0)
        assertTrue("remaining should be ~1 hour", result <= HOUR_IN_MILLIS)
    }

    @Test
    fun `returns zero when end is in the past`() {
        val now = System.currentTimeMillis()
        val result = useCase(session(now - HOUR_IN_MILLIS, now - SECOND_IN_MILLIS))
        assertEquals(0L, result)
    }

    @Test
    fun `full 16h session has correct planned duration`() {
        val startedAt = System.currentTimeMillis()
        val hours = SIXTEEN_HOURS
        val plannedEndAt = startedAt + hours * HOUR_IN_MILLIS
        val result = useCase(session(startedAt, plannedEndAt))
        val expectedMs = hours * HOUR_IN_MILLIS
        assertTrue(result!! in (expectedMs - SECOND_IN_MILLIS)..expectedMs)
    }
}
