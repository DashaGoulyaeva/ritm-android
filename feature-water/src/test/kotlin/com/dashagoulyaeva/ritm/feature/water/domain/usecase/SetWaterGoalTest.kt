package com.dashagoulyaeva.ritm.feature.water.domain.usecase

import com.dashagoulyaeva.ritm.feature.water.domain.repository.WaterGoalRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SetWaterGoalTest {
    private val repository: WaterGoalRepository = mockk(relaxed = true)
    private val useCase = SetWaterGoal(repository)

    @Test
    fun `invoke delegates goal update`() =
        runTest {
            useCase(12)

            coVerify(exactly = 1) { repository.setGoal(12) }
        }
}
