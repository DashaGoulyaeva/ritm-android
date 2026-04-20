package com.dashagoulyaeva.ritm.feature.water.domain.usecase

import com.dashagoulyaeva.ritm.feature.water.domain.repository.WaterGoalRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetWaterGoalTest {
    private val repository: WaterGoalRepository = mockk()
    private val useCase = GetWaterGoal(repository)

    @Test
    fun `invoke returns current water goal`() =
        runTest {
            every { repository.getGoal() } returns flowOf(10)

            val goal = useCase().first()

            assertEquals(10, goal)
        }
}
