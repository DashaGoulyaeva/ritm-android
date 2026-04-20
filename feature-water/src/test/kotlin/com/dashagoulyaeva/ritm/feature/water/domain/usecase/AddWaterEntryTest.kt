package com.dashagoulyaeva.ritm.feature.water.domain.usecase

import com.dashagoulyaeva.ritm.core.database.entity.WaterType
import com.dashagoulyaeva.ritm.feature.water.domain.repository.WaterRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddWaterEntryTest {
    private val repository: WaterRepository = mockk(relaxed = true)
    private val useCase = AddWaterEntry(repository)

    @Test
    fun `invoke calls repository addEntry with type WATER`() =
        runTest {
            useCase(WaterType.WATER)
            coVerify(exactly = 1) { repository.addEntry(WaterType.WATER) }
        }

    @Test
    fun `invoke calls repository addEntry with type TEA`() =
        runTest {
            useCase(WaterType.TEA)
            coVerify(exactly = 1) { repository.addEntry(WaterType.TEA) }
        }

    @Test
    fun `invoke calls repository addEntry with type COFFEE`() =
        runTest {
            useCase(WaterType.COFFEE)
            coVerify(exactly = 1) { repository.addEntry(WaterType.COFFEE) }
        }

    @Test
    fun `invoke calls repository addEntry with correct type`() =
        runTest {
            useCase(WaterType.WATER)
            coVerify(exactly = 1) { repository.addEntry(WaterType.WATER) }
            coVerify(exactly = 0) { repository.addEntry(WaterType.TEA) }
            coVerify(exactly = 0) { repository.addEntry(WaterType.COFFEE) }
        }
}
