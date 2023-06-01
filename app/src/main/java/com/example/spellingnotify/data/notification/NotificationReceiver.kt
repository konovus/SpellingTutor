package com.example.spellingnotify.data.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.annotation.RequiresApi
import androidx.core.app.RemoteInput
import com.example.spellingnotify.domain.utils.SettingsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(TIRAMISU)
@AndroidEntryPoint
class NotificationReceiver: BroadcastReceiver() {

    @Inject
    lateinit var settingsManager: SettingsManager
    @Inject
    lateinit var notificationHelper: NotificationHelper

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent) {
        GlobalScope.launch {
            when(intent.getSerializableExtra(NotificationHelper.ACTION, NotificationHelper.ActionType::class.java)) {
                NotificationHelper.ActionType.REPLY -> {
                    val remoteInput = RemoteInput.getResultsFromIntent(intent) ?: return@launch
                    val wordTyped = remoteInput.getCharSequence(NotificationHelper.WORD_TYPED).toString()
                    val wordToGuess = settingsManager.readStringSetting(SettingsManager.WORD_TO_GUESS)
                    if (wordToGuess == wordTyped.lowercase().trim()) {
                        notificationHelper.createCorrectExercisingNotification()
                    } else {
                        notificationHelper.createTryAgainExercisingNotification()
                    }
                }
                NotificationHelper.ActionType.SHOW_ANSWER -> {
                    notificationHelper.createExercisingNotificationWithAnswer()
                }
                NotificationHelper.ActionType.NEXT -> {
                    notificationHelper.createExercisingNotification()
                }
                null -> {}
            }

        }
    }
}