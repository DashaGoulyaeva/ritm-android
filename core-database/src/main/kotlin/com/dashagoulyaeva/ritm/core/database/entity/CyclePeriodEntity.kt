package com.dashagoulyaeva.ritm.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cycle_periods")
data class CyclePeriodEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startDate: String, // "yyyy-MM-dd"
    val endDate: String? = null, // null = period still ongoing
)
