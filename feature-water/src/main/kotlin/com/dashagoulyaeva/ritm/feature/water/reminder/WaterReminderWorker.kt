package com.dashagoulyaeva.ritm.feature.water.reminder

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WaterReminderWorker
    @AssistedInject
    constructor(
        @Assisted appContext: Context,
        @Assisted params: WorkerParameters,
    ) : CoroutineWorker(appContext, params) {
        override suspend fun doWork(): Result {
            showReminderNotification()
            return Result.success()
        }

        @SuppressLint("MissingPermission")
        private fun showReminderNotification() {
            createChannelIfNeeded()

            if (!hasNotificationPermission()) {
                return
            }

            val notification =
                NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Время воды")
                    .setContentText("Пора выпить стакан воды. Твой ритм это оценит.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .build()

            try {
                NotificationManagerCompat.from(applicationContext)
                    .notify(NOTIFICATION_ID, notification)
            } catch (_: SecurityException) {
                // Ignore when notifications are not allowed on the device.
            }
        }

        private fun createChannelIfNeeded() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

            val manager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val existing = manager.getNotificationChannel(CHANNEL_ID)
            if (existing != null) return

            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    "Напоминания о воде",
                    NotificationManager.IMPORTANCE_DEFAULT,
                ).apply {
                    description = "Мягкие напоминания выпить воду"
                }

            manager.createNotificationChannel(channel)
        }

        private fun hasNotificationPermission(): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
            return ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        }

        companion object {
            const val UNIQUE_WORK_NAME = "water_reminder_work"
            private const val CHANNEL_ID = "water_reminder_channel"
            private const val NOTIFICATION_ID = 1001
        }
    }
