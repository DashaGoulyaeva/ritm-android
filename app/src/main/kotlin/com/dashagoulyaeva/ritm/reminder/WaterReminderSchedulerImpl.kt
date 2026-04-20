package com.dashagoulyaeva.ritm.reminder

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.dashagoulyaeva.ritm.core.common.WaterReminderScheduler
import com.dashagoulyaeva.ritm.feature.water.reminder.WaterReminderWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WaterReminderSchedulerImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : WaterReminderScheduler {
        override fun schedule(
            enabled: Boolean,
            time: String,
        ) {
            val workManager = WorkManager.getInstance(context)
            if (!enabled) {
                workManager.cancelUniqueWork(WaterReminderWorker.UNIQUE_WORK_NAME)
                return
            }

            val initialDelayMs = computeInitialDelay(time)
            val request =
                PeriodicWorkRequestBuilder<WaterReminderWorker>(
                    REMINDER_INTERVAL_HOURS,
                    TimeUnit.HOURS,
                )
                    .setInitialDelay(initialDelayMs, TimeUnit.MILLISECONDS)
                    .build()

            workManager.enqueueUniquePeriodicWork(
                WaterReminderWorker.UNIQUE_WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                request,
            )
        }

        private fun computeInitialDelay(time: String): Long {
            val now = ZonedDateTime.now()
            val reminderTime = parseReminderTime(time)
            var next =
                now.withHour(reminderTime.hour)
                    .withMinute(reminderTime.minute)
                    .withSecond(0)
                    .withNano(0)

            if (!next.isAfter(now)) {
                next = next.plusDays(1)
            }

            return Duration.between(now, next).toMillis().coerceAtLeast(0L)
        }

        private fun parseReminderTime(time: String): LocalTime {
            return try {
                LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
            } catch (_: DateTimeParseException) {
                LocalTime.of(DEFAULT_REMINDER_HOUR, DEFAULT_REMINDER_MINUTE)
            }
        }

        private companion object {
            const val REMINDER_INTERVAL_HOURS = 24L
            const val DEFAULT_REMINDER_HOUR = 10
            const val DEFAULT_REMINDER_MINUTE = 0
        }
    }
