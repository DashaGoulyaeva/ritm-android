package com.dashagoulyaeva.ritm.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "step_daily_logs", indices = [Index(value = ["date"], unique = true)])
data class StepDailyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val stepCount: Int,
    val lastUpdatedAt: Long = System.currentTimeMillis(),
)
