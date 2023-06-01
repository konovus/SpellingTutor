package com.example.spellingnotify.presentation.ui.exerciseScreen

import com.MainDispatcherRule
import com.example.spellingnotify.data.preferences.FakeSettingManager
import com.example.spellingnotify.domain.usecases.MainFilterUseCase
import com.example.spellingnotify.domain.utils.SettingsManager
import com.example.spellingnotify.presentation.utils.ScoreBoardRow
import com.example.spellingnotify.presentation.utils.ScoreBoardRow.SCOREBOARD_DATE_FORMAT
import com.example.spellingnotify.presentation.utils.ScoreFormula
import com.example.spellingnotify.presentation.utils.formatToString
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Calendar


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class ExerciseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var settingsManager: SettingsManager
    private lateinit var mainFilterUseCase: MainFilterUseCase
    private lateinit var viewModel: ExerciseViewModel

    @Before
    fun setup() {
        settingsManager = FakeSettingManager()
        mainFilterUseCase = MainFilterUseCase(settingsManager)
        viewModel = ExerciseViewModel(mainFilterUseCase, settingsManager)
    }

    @Test
    fun `toggle scoreboard, prevValue doesn't equal with current value`() {
        assertThat(viewModel.state.value.isScoreBoardShown).isFalse()
        viewModel.toggleScoreBoard()
        assertThat(viewModel.state.value.isScoreBoardShown).isTrue()
    }

    @Test
    fun `start Game, check for gameState to be in initial state`() {
        assertThat(viewModel.state.value.isGameRunning).isFalse()
        viewModel.startGame()
        assertThat(viewModel.state.value.isGameRunning).isTrue()
        assertThat(viewModel.state.value.isEndGame).isFalse()
        assertThat(viewModel.state.value.wordTyped).isEmpty()
        assertThat(viewModel.state.value.score).isEqualTo(0)
        assertThat(viewModel.state.value.streak).isEqualTo(0)
        assertThat(viewModel.state.value.isCorrect).isNull()
        assertThat(viewModel.state.value.currentIndex).isEqualTo(0)

    }

    @Test
    fun `end Game, check for isEndGame == true and isGameRunning == false`() {
        viewModel.startGame()
        assertThat(viewModel.state.value.isEndGame).isFalse()
        assertThat(viewModel.state.value.isGameRunning).isTrue()
        viewModel.endGame()
        assertThat(viewModel.state.value.isEndGame).isTrue()
        assertThat(viewModel.state.value.isGameRunning).isFalse()
    }

    @Test
    fun `end Game, check for updated scoreBoard`() = runTest{
        val score = 1234
        viewModel.state.value = viewModel.state.value.copy(score = score)
        var scoreBoardList = settingsManager.readStringListSetting(SettingsManager.SCORE_BOARD_LIST)
        val dateTime = Calendar.getInstance().time
        val formattedDateTime = dateTime.formatToString(SCOREBOARD_DATE_FORMAT)

        var score1234IsContained = scoreBoardList.contains(ScoreBoardRow.create(formattedDateTime, score))
        assertThat(score1234IsContained).isFalse()
        viewModel.endGame()

        scoreBoardList = settingsManager.readStringListSetting(SettingsManager.SCORE_BOARD_LIST)

        score1234IsContained = scoreBoardList.contains(ScoreBoardRow.create(formattedDateTime, score))
        assertThat(score1234IsContained).isTrue()
    }

    @Test
    fun `evaluate word, word is empty , return isCorrect = null`() {
        viewModel.state.value = viewModel.state.value.copy(isCorrect = false)
        viewModel.evaluateWord("")
        assertThat(viewModel.state.value.isCorrect).isNull()
    }

    @Test
    fun `evaluate word, correct word with spaces or uppercase, returns isCorrect = true`() {
        val wordToGuess = " ${viewModel.state.value.wordModels[viewModel.state.value.currentIndex].word.uppercase()} "
        val prevScore = 3
        val prevStreak = 6
        viewModel.state.value = viewModel.state.value.copy(
            wordTyped = wordToGuess,
            score = prevScore,
            streak = prevStreak
        )
        viewModel.evaluateWord(viewModel.state.value.wordTyped)
        assertThat(viewModel.state.value.isCorrect).isTrue()
        assertThat(viewModel.state.value.wordTyped).isEqualTo(wordToGuess)
        assertThat(viewModel.state.value.score).isEqualTo(ScoreFormula.create(
            prevScore, prevStreak, wordToGuess
        ))
        assertThat(viewModel.state.value.streak).isEqualTo(prevStreak + 1)
    }

    @Test
    fun `evaluate word, incorrect word, returns isCorrect == false`() = runTest{
        val prevScore = viewModel.state.value.score
        viewModel.evaluateWord("lkjjhg7y8oihd3")
        advanceUntilIdle()
        assertThat(viewModel.state.value.isCorrect).isFalse()
        assertThat(viewModel.state.value.score).isEqualTo(prevScore - 1)
    }

    @Test
    fun `next word, assert that word typed is empty`() = runTest{
        viewModel.evaluateWord("asdlkjlkj")
        advanceUntilIdle()
        viewModel.nextWord()
        assertThat(viewModel.state.value.wordTyped).isEmpty()
    }

    @Test
    fun `next word, assert that current index == prev index + 1`() = runTest{
        val prevIndex = viewModel.state.value.currentIndex

        viewModel.evaluateWord("asdlkjlkj")
        advanceUntilIdle()
        viewModel.nextWord()
        assertThat(viewModel.state.value.currentIndex).isEqualTo(prevIndex + 1)
    }


    @Test
    fun `next word, assert that isCorrect == null`() = runTest{
        viewModel.evaluateWord("asdlkjlkj")
        advanceUntilIdle()
        viewModel.nextWord()
        assertThat(viewModel.state.value.isCorrect).isNull()
    }


    @Test
    fun `next word, assert that total == prevTotal + 1`() = runTest{
        val prevTotal = viewModel.state.value.total

        viewModel.evaluateWord("asdlkjlkj")
        advanceUntilIdle()
        viewModel.nextWord()
        assertThat(viewModel.state.value.total).isEqualTo(prevTotal + 1)
    }

    @Test
    fun `next word, assert that streak == prevStreak + 1`() = runTest{
        val prevStreak = viewModel.state.value.streak
        val prevIsCorrect = viewModel.state.value.isCorrect

        viewModel.evaluateWord("asdlkjlkj")
        advanceUntilIdle()
        viewModel.nextWord()
        assertThat(viewModel.state.value.streak).isEqualTo(
            if (prevIsCorrect == null || prevIsCorrect == false)
                0 else prevStreak
        )
    }
}