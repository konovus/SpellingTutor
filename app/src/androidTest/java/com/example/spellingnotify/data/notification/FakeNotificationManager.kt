package com.example.spellingnotify.data.notification

import com.example.spellingnotify.domain.notification.NotificationManager

class FakeNotificationManager: NotificationManager {

    private val notifications = mutableListOf<String>()

    override fun setNotification(
        hours: Int,
        minutes: Int,
        type: NotificationManager.NotificationType
    ) {
        notifications.add("$hours$minutes$type")
    }

    override fun cancelNotification(
        hours: Int,
        minutes: Int,
        type: NotificationManager.NotificationType
    ) {
        notifications.remove("$hours$minutes$type")
    }
}