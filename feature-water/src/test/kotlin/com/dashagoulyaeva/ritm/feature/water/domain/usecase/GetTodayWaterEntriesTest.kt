package com.dashagoulyaeva.ritm.feature.water.domain.usecase

import com.dashagoulyaeva.ritm.core.database.entity.WaterType
import com.dashagoulyaeva.ritm.feature.water.domain.model.WaterEntry
import com.dashagoulyaeva.ritm.feature.water.domain.repository.WaterRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetTodayWaterEntriesTest {
    private val repository: WaterRepository = mockk()
    private val useCase = GetTodayWaterEntries(repository)

    @Test
    fun `invoke returns repository flow`() =
        runTest {
            val expected =
                listOf(
                    WaterEntry(id = 1, timestamp = 100, type = WaterType.WATER),
                    WaterEntry(id = 2, timestamp = 200, type = WaterType.TEA),
                )
            every { repository.getTodayEntries() } returns flowOf(expected)

            val actual = useCase().first()

            assertEquals(expected, actual)
        }
}
