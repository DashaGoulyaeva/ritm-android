package com.dashagoulyaeva.ritm.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val iconEmoji: String = "✅",
    val colorHex: String = "#66BB6A",
    val targetDays: String = "1111111", // 7 chars, Mon-Sun, "1"=active
    val reminderTime: String? = null, // "HH:mm" or null
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
)
