package com.example.spellingnotify.data.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.annotation.RequiresApi
import androidx.core.app.RemoteInput
import com.example.spellingnotify.domain.utils.SettingsManager
import com.example.spellingnotify.presentation.redux.AppState
import com.example.spellingnotify.presentation.redux.Store
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
    @Inject
    lateinit var store: Store<AppState>

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent) {
        GlobalScope.launch {
            when(intent.getSerializableExtra(NotificationHelper.ACTION, NotificationHelper.ActionType::class.java)) {
                NotificationHelper.ActionType.REPLY -> {
                    val remoteInput = RemoteInput.getResultsFromIntent(intent) ?: return@launch
                    val wordTyped = remoteInput.getCharSequence(NotificationHelper.WORD_TYPED).toString()
                    val wordToGuess = store.state.value.wordToGuess
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
                NotificationHelper.ActionType.ARCHIVE -> {
                    val wordToLearn = store.state.value.wordToLearn
                    val currentArchivedList = settingsManager.readStringListSetting(SettingsManager.ARCHIVED_WORDS_LIST).toMutableList()
                    currentArchivedList.add(wordToLearn)

                    settingsManager.saveStringListSetting(SettingsManager.ARCHIVED_WORDS_LIST, currentArchivedList)
                    store.update { it.copy(archivedWordsList = currentArchivedList, updateArchivedWords = true) }
                    notificationHelper.createArchivedNotification()
                }
                null -> {}
            }

        }
    }
}