package com.example.spellingnotify.domain.notification

interface NotificationManager {

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

    enum class NotificationData {
        TYPE,
        HOURS,
        MINUTES
    }
}