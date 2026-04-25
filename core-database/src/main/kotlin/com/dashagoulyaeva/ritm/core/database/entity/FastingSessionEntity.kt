package com.dashagoulyaeva.ritm.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class FastingPlan { PLAN_16_8, PLAN_18_6, PLAN_20_4, PLAN_24, CUSTOM }

enum class FastingStatus { ACTIVE, COMPLETED, CANCELLED }

@Entity(tableName = "fasting_sessions")
data class FastingSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val plan: FastingPlan,
    val targetHours: Int,
    val startedAt: Long,
    val plannedEndAt: Long,
    val actualEndAt: Long? = null,
    val status: FastingStatus = FastingStatus.ACTIVE,
)
