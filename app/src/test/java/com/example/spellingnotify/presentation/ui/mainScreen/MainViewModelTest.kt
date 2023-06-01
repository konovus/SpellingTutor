package com.example.spellingnotify.presentation.ui.mainScreen

import app.cash.turbine.test
import com.MainDispatcherRule
import com.example.spellingnotify.data.api.DictionaryApi
import com.example.spellingnotify.data.preferences.FakeSettingManager
import com.example.spellingnotify.data.repository.FakeMainRepositoryImpl
import com.example.spellingnotify.domain.repository.MainRepository
import com.example.spellingnotify.domain.usecases.MainFilterUseCase
import com.example.spellingnotify.domain.utils.SettingsManager
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mainRepository: MainRepository
    private lateinit var settingsManager: SettingsManager
    private lateinit var mainFilterUseCase: MainFilterUseCase
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        mainRepository = FakeMainRepositoryImpl()
        settingsManager = FakeSettingManager()
        mainFilterUseCase = MainFilterUseCase(settingsManager)
        viewModel = MainViewModel(mainRepository, mainFilterUseCase, settingsManager)
    }

    @Test
    fun `filterList, assert that wordModels are filtered`() = runTest {
        viewModel.filterList()
        assertThat(viewModel.state.value.wordModels).isEqualTo(mainFilterUseCase.invoke())
    }

    @Test
    fun `getWordData, empty word, returns correct error message`() = runTest{
        viewModel.getWordData("")
        advanceUntilIdle()
        viewModel.event.test {
            val message = awaitItem()
            assertThat(message).isEqualTo(DictionaryApi.EMPTY_WORD)
        }
    }

    @Test
    fun `getWordData, not found word, returns correct error message`() = runTest{
        viewModel.getWordData("dfkjgjgfkd")
        advanceUntilIdle()
        viewModel.event.test {
            val message = awaitItem()
            assertThat(message).isEqualTo(DictionaryApi.WORD_NOT_FOUND)
        }
    }

    @Test
    fun `getWordData, correct word, returns wordModel with definition`() = runTest{
        viewModel.getWordData("bubble")
        advanceUntilIdle()
        val word = viewModel.state.value.wordModels.find { it.word == "bubble" }
        assertThat(word).isNotNull()
        assertThat(word!!.definition).isNotEmpty()
    }

    @Test
    fun `onEvent onSwipeWord, assert that word is added to archived list`() = runTest{
        val word = "bubble"
        val currentArchivedList = settingsManager.readStringListSetting(SettingsManager.ARCHIVED_WORDS_LIST)
        assertThat(currentArchivedList).doesNotContain(word)
        viewModel.onEvent(MainScreenActions.OnSwipeWord(word))
        advanceUntilIdle()
        val updatedArchivedList = settingsManager.readStringListSetting(SettingsManager.ARCHIVED_WORDS_LIST)
        assertThat(updatedArchivedList).contains(word)
    }

}