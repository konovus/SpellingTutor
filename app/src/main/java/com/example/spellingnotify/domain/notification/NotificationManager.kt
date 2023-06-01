package com.example.spellingnotify.domain.notification

import android.annotation.SuppressLint
import androidx.work.Operation

interface NotificationManager {

    @SuppressLint("RestrictedApi")
    fun setNotification(hours: Int, minutes: Int, type: NotificationType)

    fun cancelNotification(
        hours: Int,
        minutes: Int,
        type: NotificationType
    )

    enum class NotificationType {
        Learning,
        Exercising
    }
}