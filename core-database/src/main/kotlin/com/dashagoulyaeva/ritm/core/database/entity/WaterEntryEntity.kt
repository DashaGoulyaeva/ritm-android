package com.dashagoulyaeva.ritm.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_entries")
data class WaterEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val type: WaterType,
)

enum class WaterType { WATER, TEA, COFFEE }
