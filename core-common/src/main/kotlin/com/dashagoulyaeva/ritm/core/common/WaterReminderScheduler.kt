package com.dashagoulyaeva.ritm.core.common

interface WaterReminderScheduler {
    fun schedule(
        enabled: Boolean,
        time: String,
    )
}
