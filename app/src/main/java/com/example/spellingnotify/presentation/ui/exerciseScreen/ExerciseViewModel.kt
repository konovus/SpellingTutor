package com.example.spellingnotify.presentation.ui.exerciseScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellingnotify.domain.usecases.MainFilterUseCase
import com.example.spellingnotify.domain.utils.SettingsManager
import com.example.spellingnotify.presentation.utils.ScoreBoardRow
import com.example.spellingnotify.presentation.utils.ScoreBoardRow.SCOREBOARD_DATE_FORMAT
import com.example.spellingnotify.presentation.utils.ScoreFormula
import com.example.spellingnotify.presentation.utils.formatToString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val mainFilterUseCase: MainFilterUseCase,
    private val settingsManager: SettingsManager
): ViewModel() {

    var state: MutableState<ExerciseState> = mutableStateOf(ExerciseState())
        private set

    private val eventChannel = Channel<String>()
    val event = eventChannel.receiveAsFlow()

    private var job: Job? = null

    init {
        viewModelScope.launch {
            val scoreBoardList = settingsManager.readStringListSetting(SettingsManager.SCORE_BOARD_LIST)
            state.value = state.value.copy(
                wordModels = mainFilterUseCase().shuffled(),
                scoreBoardList = scoreBoardList.sortedByDescending { ScoreBoardRow.getScore(it) }
            )
        }
    }

    fun toggleScoreBoard() = viewModelScope.launch{
        val prevValue = state.value.isScoreBoardShown
        state.value = state.value.copy(isScoreBoardShown = !prevValue)
    }

    fun startGame() {
        state.value = state.value.copy(
            isGameRunning = true,
            isEndGame = false,
            wordTyped = "",
            score = 0,
            streak = 0,
            isCorrect = null,
            currentIndex = 0
        )
    }

    fun endGame() = viewModelScope.launch {
        if (state.value.isEndGame) return@launch
        state.value = state.value.copy(isEndGame = true, isGameRunning = false)
        val updatedList = updateScoreBoardPreferences()
        state.value = state.value.copy(scoreBoardList = updatedList.sortedByDescending { ScoreBoardRow.getScore(it) })
    }

    private suspend fun updateScoreBoardPreferences(): MutableList<String> {
        val dateTime = Calendar.getInstance().time
        val formattedDateTime = dateTime.formatToString(SCOREBOARD_DATE_FORMAT)

        val prevList = settingsManager.readStringListSetting(SettingsManager.SCORE_BOARD_LIST)
        val updatedList = prevList.toMutableList()
        updatedList.add(ScoreBoardRow.create(formattedDateTime, state.value.score))
        settingsManager.saveStringListSetting(SettingsManager.SCORE_BOARD_LIST, updatedList)
        return updatedList
    }

    fun evaluateWord(word: String) {
        state.value = state.value.copy(wordTyped = word)
        if (word.isEmpty()) {
            state.value = state.value.copy(isCorrect = null)
            job?.cancel()
            return
        }
        if (word.trim().lowercase() == state.value.wordModels[state.value.currentIndex].word) {
            val prevScore = state.value.score
            val prevStreak = state.value.streak
            val currentScore = ScoreFormula.create(prevScore, prevStreak, word)
            state.value = state.value.copy(
                isCorrect = true,
                score = currentScore,
                streak = prevStreak + 1,
            )
            job?.cancel()
            return
        }

        job?.cancel()
        job = viewModelScope.launch {
            delay(500)
            val prevScore = state.value.score
            state.value = state.value.copy(
                isCorrect = false,
                score = prevScore - 1
            )
        }
    }

    fun nextWord() {
        val prevTotal = state.value.total
        val prevIndex = state.value.currentIndex
        val prevStreak = state.value.streak
        state.value = state.value.copy(
            wordTyped = "",
            currentIndex = prevIndex + 1,
            isCorrect = null,
            total = prevTotal + 1,
            streak = if (state.value.isCorrect == null || state.value.isCorrect == false)
                0 else prevStreak
        )
    }

}