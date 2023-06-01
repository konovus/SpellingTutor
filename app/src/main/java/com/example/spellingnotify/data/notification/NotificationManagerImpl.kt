package com.example.spellingnotify.data.notification

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.spellingnotify.domain.notification.NotificationManager
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationManagerImpl(
    private val context: Context
    ) : NotificationManager {

    @SuppressLint("RestrictedApi")
    override fun setNotification(
        hours: Int,
        minutes: Int,
        type: NotificationManager.NotificationType
    ) {

        val initialDelay = calculateInitialDelay(hours, minutes)
        val data = Data(mapOf("type" to type))

        val request = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setInputData(data)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "$hours$minutes$type",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    override fun cancelNotification(
        hours: Int,
        minutes: Int,
        type: NotificationManager.NotificationType
    ) {

    WorkManager.getInstance(context).cancelUniqueWork("$hours$minutes$type")
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