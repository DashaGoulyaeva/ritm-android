package com.dashagoulyaeva.ritm.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dashagoulyaeva.ritm.core.database.entity.WaterEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterDao {
    @Insert
    suspend fun insert(entry: WaterEntryEntity)

    @Delete
    suspend fun delete(entry: WaterEntryEntity)

    @Query("SELECT * FROM water_entries WHERE timestamp >= :startMs AND timestamp < :endMs ORDER BY timestamp DESC")
    fun getEntriesForDay(
        startMs: Long,
        endMs: Long,
    ): Flow<List<WaterEntryEntity>>

    @Query("SELECT * FROM water_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<WaterEntryEntity>>
}
