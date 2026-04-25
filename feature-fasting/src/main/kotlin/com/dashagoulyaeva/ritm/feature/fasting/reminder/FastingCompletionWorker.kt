package com.dashagoulyaeva.ritm.feature.fasting.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

private const val FASTING_COMPLETION_NOTIFICATION_ID = 3001

@HiltWorker
class FastingCompletionWorker
    @AssistedInject
    constructor(
        @Assisted context: Context,
        @Assisted params: WorkerParameters,
    ) : CoroutineWorker(context, params) {
        override suspend fun doWork(): Result {
            val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "fasting_completion"
            nm.createNotificationChannel(
                NotificationChannel(channelId, "Голодание завершено", NotificationManager.IMPORTANCE_HIGH),
            )
            nm.notify(
                FASTING_COMPLETION_NOTIFICATION_ID,
                NotificationCompat.Builder(applicationContext, channelId)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Ритм — Голодание")
                    .setContentText("Период голодания завершён! Время поесть 🎉")
                    .setAutoCancel(true)
                    .build(),
            )
            return Result.success()
        }

        companion object {
            const val WORK_NAME = "fasting_completion"
        }
    }
