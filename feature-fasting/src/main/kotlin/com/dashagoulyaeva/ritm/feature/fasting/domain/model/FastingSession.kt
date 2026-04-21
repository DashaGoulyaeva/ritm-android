package com.dashagoulyaeva.ritm.feature.fasting.domain.model

enum class FastingPlan(val hours: Int, val displayName: String) {
    PLAN_16_8(16, "16:8"),
    PLAN_18_6(18, "18:6"),
    PLAN_20_4(20, "20:4"),
    PLAN_24(24, "24:0"),
    CUSTOM(0, "Свой"),
}

enum class FastingStatus { ACTIVE, COMPLETED, CANCELLED }

data class FastingSession(
    val id: Long = 0,
    val plan: FastingPlan,
    val targetHours: Int,
    val startedAt: Long,
    val plannedEndAt: Long,
    val actualEndAt: Long? = null,
    val status: FastingStatus = FastingStatus.ACTIVE,
)
