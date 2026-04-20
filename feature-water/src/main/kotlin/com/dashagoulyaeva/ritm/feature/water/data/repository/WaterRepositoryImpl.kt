package com.dashagoulyaeva.ritm.feature.water.data.repository

import com.dashagoulyaeva.ritm.core.database.dao.WaterDao
import com.dashagoulyaeva.ritm.core.database.entity.WaterEntryEntity
import com.dashagoulyaeva.ritm.core.database.entity.WaterType
import com.dashagoulyaeva.ritm.feature.water.domain.model.WaterEntry
import com.dashagoulyaeva.ritm.feature.water.domain.repository.WaterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WaterRepositoryImpl
    @Inject
    constructor(
        private val dao: WaterDao,
    ) : WaterRepository {
        override fun getTodayEntries(): Flow<List<WaterEntry>> {
            val zoneId = ZoneId.systemDefault()
            val today = LocalDate.now()
            val startMs = today.atStartOfDay(zoneId).toInstant().toEpochMilli()
            val endMs = today.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli()
            return dao.getEntriesForDay(startMs, endMs).map { list -> list.map { it.toDomain() } }
        }

        override fun getAllEntries(): Flow<List<WaterEntry>> {
            return dao.getAllEntries().map { list -> list.map { it.toDomain() } }
        }

        override suspend fun addEntry(type: WaterType) {
            dao.insert(
                WaterEntryEntity(
                    timestamp = System.currentTimeMillis(),
                    type = type,
                ),
            )
        }

        override suspend fun deleteEntry(entry: WaterEntry) {
            dao.delete(
                WaterEntryEntity(
                    id = entry.id,
                    timestamp = entry.timestamp,
                    type = entry.type,
                ),
            )
        }

        private fun WaterEntryEntity.toDomain(): WaterEntry = WaterEntry(id = id, timestamp = timestamp, type = type)
    }
