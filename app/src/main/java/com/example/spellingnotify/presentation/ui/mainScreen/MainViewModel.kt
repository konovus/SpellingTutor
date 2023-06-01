package com.example.spellingnotify.presentation.ui.mainScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellingnotify.data.utils.Resource
import com.example.spellingnotify.domain.models.WordModel
import com.example.spellingnotify.domain.repository.MainRepository
import com.example.spellingnotify.domain.usecases.MainFilterUseCase
import com.example.spellingnotify.domain.utils.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val mainFilterUseCase: MainFilterUseCase,
    private val settingsManager: SettingsManager
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
        val response = mainRepository.fetchWordData(word)
        processNetworkResult(response) {
            val wordModels = state.value.wordModels.toMutableList()
            val index = wordModels.indexOf(WordModel(word = word, definition = ""))
            if (index == -1) return@processNetworkResult
            wordModels.removeIf { it.word == word }
            wordModels.add(index, it)
            state.value = state.value.copy(wordModels = wordModels)
        }
    }

    fun onEvent(action: MainScreenActions) = viewModelScope.launch {
        when(action) {
            is MainScreenActions.OnSwipeWord -> {
                val currentArchivedList = settingsManager.readStringListSetting(SettingsManager.ARCHIVED_WORDS_LIST).toMutableList()
                currentArchivedList.add(action.word)
                settingsManager.saveStringListSetting(SettingsManager.ARCHIVED_WORDS_LIST, currentArchivedList)
                val currentWordList = state.value.wordModels
                state.value = state.value.copy(wordModels = currentWordList.filterNot { currentArchivedList.contains(it.word) })
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
            is Resource.Error -> sendEvent(message = result.message.orEmpty())
        }
    }
}