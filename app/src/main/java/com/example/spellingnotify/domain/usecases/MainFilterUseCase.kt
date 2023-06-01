package com.example.spellingnotify.domain.usecases

import com.example.spellingnotify.data.local.WordsData
import com.example.spellingnotify.domain.filters.WordsFilter
import com.example.spellingnotify.domain.models.WordModel
import com.example.spellingnotify.domain.utils.SettingsManager
import javax.inject.Inject

class MainFilterUseCase @Inject constructor(
    private val settingsManager: SettingsManager,
) {

    suspend operator fun invoke(): List<WordModel> {
        val filters = retrieveFiltersFromPreferences()
        val filteredWords = passThroughAllFilters(filters)
        return checkForArchivedWords(filteredWords)
    }

    private suspend fun checkForArchivedWords(filteredWords: List<WordModel>): List<WordModel> {
        val archivedWords =
            settingsManager.readStringListSetting(SettingsManager.ARCHIVED_WORDS_LIST)
        return filteredWords.filterNot { archivedWords.contains(it.word) }
    }

    private fun passThroughAllFilters(filters: List<WordsFilter>): List<WordModel> {
        return WordsData.allWords.mapNotNull { word ->
            var returnWordModel: WordModel? = null
            filters.forEach {
                returnWordModel = if (!it.value && !it.filter(word)) {
                    WordModel(word = word, definition = "")
                } else if (it.value) {
                    WordModel(word = word, definition = "")
                } else return@mapNotNull null
            }
            returnWordModel
        }
    }

    private suspend fun retrieveFiltersFromPreferences(): List<WordsFilter> {
        return WordsFilter.allFilters.map {
             it.apply { value = settingsManager.readBooleanSetting(it.text) }
        }
    }
}