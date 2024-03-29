package com.example.spellingnotify.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.example.spellingnotify.R
import com.example.spellingnotify.data.di.TAG
import com.example.spellingnotify.domain.repository.MainRepository
import com.example.spellingnotify.domain.usecases.MainFilterUseCase
import com.example.spellingnotify.presentation.redux.AppState
import com.example.spellingnotify.presentation.redux.Store
import com.example.spellingnotify.presentation.utils.distinctChars
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(DelicateCoroutinesApi::class)
class NotificationHelper @Inject constructor(
    private val context: Context,
    private val store: Store<AppState>,
    private val repository: MainRepository,
    private val mainFilterUseCase: MainFilterUseCase
) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    enum class ActionType {
        REPLY,
        SHOW_ANSWER,
        NEXT,
        ARCHIVE
    }

    companion object {
        const val WORD_TYPED = "wordTyped"
        const val CHANNEL_ID_LEARNING = "101"
        const val CHANNEL_ID_EXERCISING = "102"
        const val ACTION = "action"
        const val ID_LEARNING = 1
        const val ID_EXERCISING = 2
    }

    fun createLearningNotification() = GlobalScope.launch{
        val notificationChannel =
            NotificationChannel(CHANNEL_ID_LEARNING, "learning", NotificationManager.IMPORTANCE_HIGH)

        notificationManager.createNotificationChannel(notificationChannel)

        val word = mainFilterUseCase().random().word
        Log.i(TAG, "createLearningNotification: $word")
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID_LEARNING)
            .setContentTitle("Learning")
            .setContentText("Word of the day: $word")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .addAction(getArchiveAction())

        notificationManager.notify(ID_LEARNING, notificationBuilder.build())
        store.update { it.copy(wordToLearn = word) }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun createExercisingNotification() = GlobalScope.launch {
        val notificationChannel =
            NotificationChannel(CHANNEL_ID_EXERCISING, "exercising", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)

        val word = mainFilterUseCase().random().word
        Log.i(TAG, "createExercisingNotification: $word")
        var definition = "No definition found"
        val response = repository.fetchWordData(word)
        if (response.data != null)
            definition = response.data.definition

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID_EXERCISING)
            .setContentTitle("Exercising")
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                "Word to type: ${word.distinctChars()}\n" +
                "Definition: $definition"))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .addAction(getTypeAction())
            .addAction(getShowAnswerAction())
            .addAction(getNextAction())

        notificationManager.notify(ID_EXERCISING, notificationBuilder.build())
        store.update { it.copy(wordToGuess = word) }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun createTryAgainExercisingNotification() = GlobalScope.launch {
        val notificationChannel =
            NotificationChannel(CHANNEL_ID_EXERCISING, "exercising", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)

        val word = store.state.value.wordToGuess
        var definition = "No definition found"
        val response = repository.fetchWordData(word)
        if (response.data != null)
            definition = response.data.definition

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID_EXERCISING)
            .setContentTitle("Exercising")
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                "Word to type: ${word.distinctChars()}\n" +
                "Definition: $definition"))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .addAction(getTryAgainAction())
            .addAction(getShowAnswerAction())
            .addAction(getNextAction())

        notificationManager.notify(ID_EXERCISING, notificationBuilder.build())
        store.update { it.copy(wordToGuess = word) }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun createCorrectExercisingNotification() = GlobalScope.launch {
        val notificationChannel =
            NotificationChannel(CHANNEL_ID_EXERCISING, "exercising", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID_EXERCISING)
            .setContentTitle("Correct")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .addAction(getNextAction())

        notificationManager.notify(ID_EXERCISING, notificationBuilder.build())
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun createArchivedNotification() = GlobalScope.launch {
        val word = store.state.value.wordToLearn

        val notificationChannel =
            NotificationChannel(CHANNEL_ID_LEARNING, "archive", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)


        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID_LEARNING)
            .setContentTitle("Word: $word was archived")
            .setSmallIcon(R.drawable.ic_launcher_background)

        notificationManager.notify(ID_LEARNING, notificationBuilder.build())
    }

    fun createExercisingNotificationWithAnswer() = GlobalScope.launch {
        val word = store.state.value.wordToGuess

        val notificationChannel =
            NotificationChannel(CHANNEL_ID_EXERCISING, "exercising", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID_EXERCISING)
            .setContentTitle("Answer")
            .setContentText("Correct word: $word")
            .setSmallIcon(R.drawable.ic_launcher_background)

        notificationManager.notify(ID_EXERCISING, notificationBuilder.build())
    }

    private fun getTypeAction(): NotificationCompat.Action {
        val remoteInput = RemoteInput.Builder(WORD_TYPED).build()
        return NotificationCompat.Action.Builder(
            android.R.drawable.ic_input_add,
            "Type...", getResultPendingIntent(ActionType.REPLY))
            .addRemoteInput(remoteInput)
            .build()
    }

    private fun getTryAgainAction(): NotificationCompat.Action {
        val remoteInput = RemoteInput.Builder(WORD_TYPED).build()

        return NotificationCompat.Action.Builder(
            android.R.drawable.ic_input_add,
            "Try again...", getResultPendingIntent(ActionType.REPLY))
            .addRemoteInput(remoteInput)
            .build()
    }

    private fun getShowAnswerAction(): NotificationCompat.Action {
        return NotificationCompat.Action.Builder(
            null,
            "Show answer", getResultPendingIntent(ActionType.SHOW_ANSWER))
            .build()
    }

    private fun getNextAction(): NotificationCompat.Action {
        return NotificationCompat.Action.Builder(
            null,
            "Next word", getResultPendingIntent(ActionType.NEXT))
            .build()
    }

    private fun getArchiveAction(): NotificationCompat.Action {
        return NotificationCompat.Action.Builder(
            null,
            "Archive", getResultPendingIntent(ActionType.ARCHIVE))
            .build()
    }

    private fun getResultPendingIntent(extra: ActionType? = null): PendingIntent  {
        val requestCode = extra?.hashCode() ?: 0
        val resultIntent = Intent(context, NotificationReceiver::class.java)
        resultIntent.putExtra(ACTION, extra)
        resultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            resultIntent,
            PendingIntent.FLAG_MUTABLE
        )
    }
}