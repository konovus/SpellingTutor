package com.example.spellingnotify.data.notification

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.spellingnotify.domain.notification.NotificationManager
import com.example.spellingnotify.domain.notification.NotificationManager.NotificationData
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationManagerImpl(
    private val context: Context
) : NotificationManager {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun setNotification(
        hours: Int,
        minutes: Int,
        type: NotificationManager.NotificationType
    ) {

        val initialDelay = calculateInitialDelay(hours, minutes)
        val data = Data.Builder().putAll(
            mapOf(
                NotificationData.TYPE.name to type.toString(),
                NotificationData.HOURS.name to hours,
                NotificationData.MINUTES.name to minutes
            )
        ).build()

        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(data)
            .addTag("$hours$minutes$type")
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }

    override fun cancelNotification(
        hours: Int,
        minutes: Int,
        type: NotificationManager.NotificationType
    ) {

        WorkManager.getInstance(context).cancelAllWorkByTag("$hours$minutes$type")
    }

    private fun calculateInitialDelay(hours: Int, minutes: Int): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, minutes)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (now.after(target)) {
            target.add(Calendar.DATE, 1)
        }
        return target.timeInMillis - now.timeInMillis
    }
}