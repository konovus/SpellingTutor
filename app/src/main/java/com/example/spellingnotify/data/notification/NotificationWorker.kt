package com.example.spellingnotify.data.notification

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.spellingnotify.domain.notification.NotificationManager
import com.example.spellingnotify.domain.notification.NotificationManager.NotificationType.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationHelper: NotificationHelper
): Worker(context, workerParams) {

    override fun doWork(): Result {
        when(inputData.keyValueMap["type"] as NotificationManager.NotificationType) {
            Learning -> notificationHelper.createLearningNotification()
            Exercising -> notificationHelper.createExercisingNotification()
        }
        return Result.success()
    }
}