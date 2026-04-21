package com.dashagoulyaeva.ritm.feature.habits.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class HabitReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "habit_reminder"
        nm.createNotificationChannel(
            NotificationChannel(channelId, "Напоминания о привычках", NotificationManager.IMPORTANCE_DEFAULT)
        )
        nm.notify(
            2001,
            NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Ритм — Привычки")
                .setContentText("Не забудьте отметить привычки сегодня!")
                .setAutoCancel(true)
                .build(),
        )
        return Result.success()
    }

    companion object {
        const val WORK_NAME = "habit_reminder_daily"
    }
}
