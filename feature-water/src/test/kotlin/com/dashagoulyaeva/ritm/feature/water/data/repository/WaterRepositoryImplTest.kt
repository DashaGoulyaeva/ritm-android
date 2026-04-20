package com.dashagoulyaeva.ritm.feature.water.data.repository

import com.dashagoulyaeva.ritm.core.database.dao.WaterDao
import com.dashagoulyaeva.ritm.core.database.entity.WaterEntryEntity
import com.dashagoulyaeva.ritm.core.database.entity.WaterType
import com.dashagoulyaeva.ritm.feature.water.domain.model.WaterEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class WaterRepositoryImplTest {
    private val fakeDao = FakeWaterDao()
    private val repository = WaterRepositoryImpl(fakeDao)

    @Test
    fun `getTodayEntries uses current day range and maps entities`() =
        runTest {
            val zoneId = ZoneId.systemDefault()
            val today = LocalDate.now()
            val startMs = today.atStartOfDay(zoneId).toInstant().toEpochMilli()
            val endMs = today.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli()

            fakeDao.entries += WaterEntryEntity(id = 1, timestamp = startMs, type = WaterType.WATER)
            fakeDao.entries += WaterEntryEntity(id = 2, timestamp = startMs + 1_000, type = WaterType.TEA)
            fakeDao.entries += WaterEntryEntity(id = 3, timestamp = startMs - 1, type = WaterType.COFFEE)
            fakeDao.entries += WaterEntryEntity(id = 4, timestamp = endMs, type = WaterType.WATER)

            val result = repository.getTodayEntries().first()

            assertEquals(startMs, fakeDao.lastStartMs)
            assertEquals(endMs, fakeDao.lastEndMs)
            assertEquals(2, result.size)
            assertEquals(2L, result[0].id)
            assertEquals(1L, result[1].id)
        }

    @Test
    fun `getAllEntries maps entities sorted by timestamp desc`() =
        runTest {
            fakeDao.entries += WaterEntryEntity(id = 1, timestamp = 100, type = WaterType.WATER)
            fakeDao.entries += WaterEntryEntity(id = 2, timestamp = 300, type = WaterType.COFFEE)
            fakeDao.entries += WaterEntryEntity(id = 3, timestamp = 200, type = WaterType.TEA)

            val result = repository.getAllEntries().first()

            assertEquals(listOf(2L, 3L, 1L), result.map(WaterEntry::id))
        }

    @Test
    fun `addEntry inserts entity with matching type and current timestamp`() =
        runTest {
            val before = System.currentTimeMillis()
            repository.addEntry(WaterType.TEA)
            val after = System.currentTimeMillis()

            val inserted = fakeDao.lastInserted
            assertNotNull(inserted)
            assertEquals(WaterType.TEA, inserted?.type)
            assertTrue(inserted!!.timestamp in before..after)
        }

    @Test
    fun `deleteEntry delegates matching entity to dao`() =
        runTest {
            val entry = WaterEntry(id = 11, timestamp = 500L, type = WaterType.COFFEE)

            repository.deleteEntry(entry)

            assertEquals(entry.id, fakeDao.lastDeleted?.id)
            assertEquals(entry.timestamp, fakeDao.lastDeleted?.timestamp)
            assertEquals(entry.type, fakeDao.lastDeleted?.type)
        }

    private class FakeWaterDao : WaterDao {
        val entries: MutableList<WaterEntryEntity> = mutableListOf()
        var lastStartMs: Long? = null
        var lastEndMs: Long? = null
        var lastInserted: WaterEntryEntity? = null
        var lastDeleted: WaterEntryEntity? = null

        override suspend fun insert(entry: WaterEntryEntity) {
            lastInserted = entry
            entries += entry.copy(id = if (entry.id == 0L) entries.size.toLong() + 1 else entry.id)
        }

        override suspend fun delete(entry: WaterEntryEntity) {
            lastDeleted = entry
            entries.removeAll { it.id == entry.id }
        }

        override fun getEntriesForDay(
            startMs: Long,
            endMs: Long,
        ): Flow<List<WaterEntryEntity>> {
            lastStartMs = startMs
            lastEndMs = endMs
            return flowOf(
                entries
                    .filter { it.timestamp in startMs until endMs }
                    .sortedByDescending { it.timestamp },
            )
        }

        override fun getAllEntries(): Flow<List<WaterEntryEntity>> {
            return flowOf(entries.sortedByDescending { it.timestamp })
        }
    }
}
