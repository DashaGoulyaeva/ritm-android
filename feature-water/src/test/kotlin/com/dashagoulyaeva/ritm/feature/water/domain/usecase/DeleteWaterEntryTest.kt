package com.dashagoulyaeva.ritm.feature.water.domain.usecase

import com.dashagoulyaeva.ritm.core.database.entity.WaterType
import com.dashagoulyaeva.ritm.feature.water.domain.model.WaterEntry
import com.dashagoulyaeva.ritm.feature.water.domain.repository.WaterRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteWaterEntryTest {
    private val repository: WaterRepository = mockk(relaxed = true)
    private val useCase = DeleteWaterEntry(repository)

    @Test
    fun `invoke delegates delete to repository`() =
        runTest {
            val entry = WaterEntry(id = 5, timestamp = 123L, type = WaterType.COFFEE)

            useCase(entry)

            coVerify(exactly = 1) { repository.deleteEntry(entry) }
        }
}
