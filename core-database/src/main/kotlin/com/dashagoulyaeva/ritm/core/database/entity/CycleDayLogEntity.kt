package com.dashagoulyaeva.ritm.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

enum class FlowIntensity { NONE, LIGHT, MEDIUM, HEAVY }
enum class MoodLevel { UNKNOWN, GREAT, GOOD, NEUTRAL, LOW, AWFUL }

@Entity(tableName = "cycle_day_logs", indices = [Index(value = ["date"], unique = true)])
data class CycleDayLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // "yyyy-MM-dd"
    val flow: FlowIntensity = FlowIntensity.NONE,
    val mood: MoodLevel = MoodLevel.UNKNOWN,
    val symptoms: String = "", // comma-separated symptom keys
    val note: String = "",
)
