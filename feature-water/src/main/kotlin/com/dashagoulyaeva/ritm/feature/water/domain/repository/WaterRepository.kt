package com.dashagoulyaeva.ritm.feature.water.domain.repository

import com.dashagoulyaeva.ritm.core.database.entity.WaterType
import com.dashagoulyaeva.ritm.feature.water.domain.model.WaterEntry
import kotlinx.coroutines.flow.Flow

interface WaterRepository {
    fun getTodayEntries(): Flow<List<WaterEntry>>

    fun getAllEntries(): Flow<List<WaterEntry>>

    suspend fun addEntry(type: WaterType)

    suspend fun deleteEntry(entry: WaterEntry)
}
