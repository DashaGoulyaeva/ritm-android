package com.dashagoulyaeva.ritm.feature.fasting.domain.model

private const val PLAN_16_8_HOURS = 16
private const val PLAN_18_6_HOURS = 18
private const val PLAN_20_4_HOURS = 20
private const val PLAN_24_HOURS = 24
private const val CUSTOM_PLAN_HOURS = 0

enum class FastingPlan(val hours: Int, val displayName: String) {
    PLAN_16_8(PLAN_16_8_HOURS, "16:8"),
    PLAN_18_6(PLAN_18_6_HOURS, "18:6"),
    PLAN_20_4(PLAN_20_4_HOURS, "20:4"),
    PLAN_24(PLAN_24_HOURS, "24:0"),
    CUSTOM(CUSTOM_PLAN_HOURS, "Свой"),
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
