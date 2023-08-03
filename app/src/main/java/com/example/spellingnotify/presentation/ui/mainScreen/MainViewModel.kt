package com.example.spellingnotify.presentation.ui.mainScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellingnotify.data.utils.Resource
import com.example.spellingnotify.domain.models.WordModel
import com.example.spellingnotify.domain.repository.MainRepository
import com.example.spellingnotify.domain.usecases.MainFilterUseCase
import com.example.spellingnotify.domain.utils.SettingsManager
import com.example.spellingnotify.presentation.redux.AppState
import com.example.spellingnotify.presentation.redux.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val mainFilterUseCase: MainFilterUseCase,
    private val settingsManager: SettingsManager,
    private val store: Store<AppState>,

    ) : ViewModel() {

    var state = mutableStateOf(MainState())
        private set

    private val eventChannel = Channel<String>()
    val event = eventChannel.receiveAsFlow()

    init {
        filterList()
    }

    fun filterList() = viewModelScope.launch {
        state.value = state.value.copy(wordModels = mainFilterUseCase())
    }

    fun getWordData(word: String) = viewModelScope.launch {
        val wordsClicked = state.value.wordsClicked.toMutableList()
        val wordModels = state.value.wordModels.toMutableList()
        val index = wordModels.map { it.word }.indexOf(word)
        if (index == -1) {
            sendEvent("No word: $word found")
            return@launch
        }
        if (wordDefinitionIsAlreadyContained(index, wordModels, word, wordsClicked)) return@launch
        if (wordIsAlreadyOpen(word, wordsClicked)) return@launch

        state.value = state.value.copy(isLoading = true, currentWordClicked = word)
        fetchWordData(word, index, wordModels, wordsClicked)
    }

    private suspend fun fetchWordData(
        word: String,
        index: Int,
        wordModels: MutableList<WordModel>,
        wordsClicked: MutableList<String>
    ) {
        val response = mainRepository.fetchWordData(word)
        processNetworkResult(response) {
            wordModels.removeIf { it.word == word }
            wordModels.add(index, it)
            wordsClicked.add(word)
            state.value = state.value.copy(
                wordModels = wordModels,
                isLoading = false,
                currentWordClicked = null,
                wordsClicked = wordsClicked
            )
        }
    }

    private fun wordIsAlreadyOpen(
        word: String,
        wordsClicked: MutableList<String>
    ): Boolean {
        if (state.value.wordsClicked.contains(word)) {
            wordsClicked.remove(word)
            state.value = state.value.copy(wordsClicked = wordsClicked)
            return true
        }
        return false
    }

    private fun wordDefinitionIsAlreadyContained(
        index: Int,
        wordModels: MutableList<WordModel>,
        word: String,
        wordsClicked: MutableList<String>
    ): Boolean {
        if (index != -1 && wordModels[index].definition.isNotEmpty()
            && !state.value.wordsClicked.contains(word)
        ) {
            wordsClicked.add(word)
            state.value = state.value.copy(wordsClicked = wordsClicked)
            return true
        }
        return false
    }

    fun onEvent(action: MainScreenActions) = viewModelScope.launch {
        when (action) {
            is MainScreenActions.OnSwipeWord -> {
                val currentArchivedList =
                    settingsManager.readStringListSetting(SettingsManager.ARCHIVED_WORDS_LIST)
                        .toMutableList()
                currentArchivedList.add(action.word)
                settingsManager.saveStringListSetting(
                    SettingsManager.ARCHIVED_WORDS_LIST,
                    currentArchivedList
                )
                store.update { it.copy(archivedWordsList = currentArchivedList, updateArchivedWords = true) }
                val currentWordList = state.value.wordModels
                state.value = state.value.copy(wordModels = currentWordList.filterNot {
                    currentArchivedList.contains(it.word)
                })
            }
        }
    }

    private fun sendEvent(message: String) = viewModelScope.launch {
        eventChannel.send(message)
    }

    private fun <T> processNetworkResult(
        result: Resource<T>,
        processBlock: suspend (T) -> Unit
    ) = viewModelScope.launch {
        when (result) {
            is Resource.Success -> processBlock(result.data!!)
            is Resource.Error -> {
                state.value = state.value.copy(isLoading = false, currentWordClicked = null)
                sendEvent(message = result.message.orEmpty())
            }
        }
    }
}