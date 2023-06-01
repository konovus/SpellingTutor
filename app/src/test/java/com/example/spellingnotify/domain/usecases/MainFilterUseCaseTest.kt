package com.example.spellingnotify.domain.usecases

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.spellingnotify.data.local.WordsData
import com.example.spellingnotify.data.preferences.FakeSettingManager
import com.example.spellingnotify.domain.filters.WordsFilter
import com.example.spellingnotify.domain.utils.SettingsManager
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class MainFilterUseCaseTest {

    private lateinit var settingsManager: SettingsManager
    private lateinit var mainFilterUseCase: MainFilterUseCase

    @Before
    fun setup() {
        settingsManager = FakeSettingManager()
        mainFilterUseCase = MainFilterUseCase(settingsManager)
    }

    @Test
    fun `all filters are off, returns empty list` () = runTest{
        WordsFilter.allFilters.forEach {
             settingsManager.saveBooleanSetting(it.text, false)
        }

        val wordModels = mainFilterUseCase.invoke()
        assertThat(wordModels).isEmpty()
    }

    @Test
    fun `all filters are on, returns list with all words` () = runTest{
        WordsFilter.allFilters.forEach {
             settingsManager.saveBooleanSetting(it.text, true)
        }

        val wordModels = mainFilterUseCase.invoke()
        assertThat(wordModels.size).isEqualTo(WordsData.allWords.size)
    }

    @Test
    fun `three letter words filter is off, returns list with no three letter words` () = runTest{
        WordsFilter.allFilters.forEach {
             settingsManager.saveBooleanSetting(it.text, it.text != "Three letter words")
        }

        val wordModels = mainFilterUseCase.invoke()
        val wordModelsContainThreeLetterWords = wordModels.any { it.word.length == 3 }
        assertThat(wordModelsContainThreeLetterWords).isFalse()
    }

    @Test
    fun `add all app are archived, returns list without add all app words` () = runTest{
        WordsFilter.allFilters.forEach {
             settingsManager.saveBooleanSetting(it.text, true)
        }

        settingsManager.saveStringListSetting(SettingsManager.ARCHIVED_WORDS_LIST,
                listOf("add", "all", "app")
            )

        val wordModels = mainFilterUseCase.invoke()
        val wordModelsContainThreeLetterWords = wordModels.map { it.word }.none {
            it == "add" || it == "all" || it == "app"
        }
        assertThat(wordModelsContainThreeLetterWords).isTrue()
    }
}