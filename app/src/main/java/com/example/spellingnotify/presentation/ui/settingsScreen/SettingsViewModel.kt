package com.example.spellingnotify.presentation.ui.settingsScreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellingnotify.domain.filters.WordsFilter
import com.example.spellingnotify.domain.notification.NotificationManager
import com.example.spellingnotify.domain.utils.SettingsManager
import com.example.spellingnotify.presentation.redux.AppState
import com.example.spellingnotify.presentation.redux.Store
import com.example.spellingnotify.presentation.utils.SettingTimeInterval
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager,
    private val notificationManager: NotificationManager,
    private val store: Store<AppState>,
) : ViewModel() {

    var settingsState = mutableStateOf(SettingsState())
        private set

    init {
        viewModelScope.launch {
            val archivedWordsList =
                settingsManager.readStringListSetting(SettingsManager.ARCHIVED_WORDS_LIST)
            val learningIntervalsList =
                settingsManager.readStringListSetting(SettingsManager.LEARNING_INTERVALS_LIST)
            val exercisingIntervalsList =
                settingsManager.readStringListSetting(SettingsManager.EXERCISING_INTERVALS_LIST)
            val wordsFiltersList = WordsFilter.allFilters.map {
                    it.apply { value = settingsManager.readBooleanSetting(it.text) }
            }
            settingsState.value = settingsState.value.copy(
                learningIntervalsList = learningIntervalsList,
                exercisingIntervalsList = exercisingIntervalsList,
                wordsFiltersList = wordsFiltersList,
                wordsFiltersListPreliminary = wordsFiltersList.toMutableStateList(),
                archivedWordsList = archivedWordsList
            )
            store.state.collectLatest { appState ->
                if (!appState.updateArchivedWords) return@collectLatest
                settingsState.value = settingsState.value.copy(
                    archivedWordsList = appState.archivedWordsList,
                    archivedWordsListPreliminary = appState.archivedWordsList,
                )
                store.update { it.copy(updateArchivedWords = false) }
            }
        }

    }

    fun onAction(action: SettingsActions) = viewModelScope.launch {
        when (action) {
            is SettingsActions.OnToggleLearningIntervalsDialog -> {
                val prevValue = settingsState.value.isLearningIntervalsDialogShown
                settingsState.value = settingsState.value.copy(
                    isLearningIntervalsDialogShown = !prevValue,
                    learningIntervalsListPreliminary = settingsState.value.learningIntervalsList
                )
            }

            is SettingsActions.OnEditLearningInterval -> {
                val intervals = settingsState.value.learningIntervalsListPreliminary.toMutableList()
                intervals.removeAt(action.index)
                intervals.add(
                    action.index,
                    SettingTimeInterval.create(action.hours, action.minutes)
                )
                settingsState.value =
                    settingsState.value.copy(learningIntervalsListPreliminary = intervals)
            }

            is SettingsActions.OnAddLearningInterval -> {
                val intervals = settingsState.value.learningIntervalsListPreliminary.toMutableList()
                intervals.add(SettingTimeInterval.create(action.hours, action.minutes))
                settingsState.value =
                    settingsState.value.copy(learningIntervalsListPreliminary = intervals)
            }

            is SettingsActions.OnDeleteLearningInterval -> {
                val intervals = settingsState.value.learningIntervalsListPreliminary.toMutableList()
                intervals.removeAt(action.index)
                settingsState.value =
                    settingsState.value.copy(learningIntervalsListPreliminary = intervals)
            }

            is SettingsActions.OnApplyLearningIntervalsChanges -> {
                settingsState.value.learningIntervalsListPreliminary.forEach { interval ->
                    if (!settingsState.value.learningIntervalsList.contains(interval)) {
                        notificationManager.setNotification(
                            SettingTimeInterval.getHour(interval),
                            SettingTimeInterval.getMinute(interval),
                            NotificationManager.NotificationType.Learning
                        )
                    }
                }
                settingsState.value.learningIntervalsList.forEach { interval ->
                    if (!settingsState.value.learningIntervalsListPreliminary.contains(interval)) {
                        notificationManager.cancelNotification(
                            SettingTimeInterval.getHour(interval),
                            SettingTimeInterval.getMinute(interval),
                            NotificationManager.NotificationType.Learning
                        )
                    }
                }
                settingsManager.saveStringListSetting(
                    SettingsManager.LEARNING_INTERVALS_LIST,
                    settingsState.value.learningIntervalsListPreliminary
                )
                settingsState.value = settingsState.value.copy(
                    isLearningIntervalsDialogShown = false,
                    learningIntervalsList = settingsState.value.learningIntervalsListPreliminary
                )
            }

            is SettingsActions.OnToggleWordsFilterDialog -> {
                val prevValue = settingsState.value.isWordsFilterDialogShown
                settingsState.value = settingsState.value.copy(
                    isWordsFilterDialogShown = !prevValue,
                    wordsFiltersListPreliminary = settingsState.value.wordsFiltersList.toMutableStateList()
                )
            }

            is SettingsActions.OnApplyWordsFiltersChanges -> {
                settingsState.value.wordsFiltersListPreliminary.forEach {
                    settingsManager.saveBooleanSetting(it.text, it.value)
                }
                settingsState.value = settingsState.value.copy(
                    wordsFiltersList = settingsState.value.wordsFiltersListPreliminary,
                    isWordsFilterDialogShown = false
                )
            }

            is SettingsActions.OnToggleWordsFilter -> {
                val wordsFilters =
                    settingsState.value.wordsFiltersListPreliminary.toMutableStateList()
                val prevValue = wordsFilters[action.index].value
                val updatedFilter =
                    WordsFilter(text = wordsFilters[action.index].text, value = !prevValue)
                wordsFilters.removeAt(action.index)
                wordsFilters.add(action.index, updatedFilter)
                settingsState.value =
                    settingsState.value.copy(wordsFiltersListPreliminary = wordsFilters)
            }

            is SettingsActions.OnAddExercisingInterval -> {
                val intervals =
                    settingsState.value.exercisingIntervalsListPreliminary.toMutableList()
                intervals.add(SettingTimeInterval.create(action.hours, action.minutes))
                settingsState.value =
                    settingsState.value.copy(exercisingIntervalsListPreliminary = intervals)
            }

            is SettingsActions.OnApplyExercisingIntervalsChanges -> {
                settingsState.value.exercisingIntervalsListPreliminary.forEach { interval ->
                    if (!settingsState.value.exercisingIntervalsList.contains(interval)) {
                        notificationManager.setNotification(
                            SettingTimeInterval.getHour(interval),
                            SettingTimeInterval.getMinute(interval),
                            NotificationManager.NotificationType.Exercising
                        )
                    }
                }
                settingsState.value.exercisingIntervalsList.forEach { interval ->
                    if (!settingsState.value.exercisingIntervalsListPreliminary.contains(interval)) {
                        notificationManager.cancelNotification(
                            SettingTimeInterval.getHour(interval),
                            SettingTimeInterval.getMinute(interval),
                            NotificationManager.NotificationType.Exercising
                        )
                    }
                }
                settingsManager.saveStringListSetting(
                    SettingsManager.EXERCISING_INTERVALS_LIST,
                    settingsState.value.exercisingIntervalsListPreliminary
                )
                settingsState.value = settingsState.value.copy(
                    isExercisingIntervalsDialogShown = false,
                    exercisingIntervalsList = settingsState.value.exercisingIntervalsListPreliminary
                )
            }

            is SettingsActions.OnDeleteExercisingInterval -> {
                val intervals =
                    settingsState.value.exercisingIntervalsListPreliminary.toMutableList()
                intervals.removeAt(action.index)
                settingsState.value =
                    settingsState.value.copy(exercisingIntervalsListPreliminary = intervals)
            }

            is SettingsActions.OnEditExercisingInterval -> {
                val intervals =
                    settingsState.value.exercisingIntervalsListPreliminary.toMutableList()
                intervals.removeAt(action.index)
                intervals.add(
                    action.index,
                    SettingTimeInterval.create(action.hours, action.minutes)
                )
                settingsState.value =
                    settingsState.value.copy(exercisingIntervalsListPreliminary = intervals)
            }

            is SettingsActions.OnToggleExercisingIntervalsDialog -> {
                val prevValue = settingsState.value.isExercisingIntervalsDialogShown
                settingsState.value = settingsState.value.copy(
                    isExercisingIntervalsDialogShown = !prevValue,
                    exercisingIntervalsListPreliminary = settingsState.value.exercisingIntervalsList
                )
            }

            is SettingsActions.OnPermissionRequiredSettingClick -> {
                settingsState.value = settingsState.value.copy(
                    permissionRequiredSetting = action.setting
                )
            }

            is SettingsActions.OnApplyArchivedWordsChanges -> {
                val prevArchivedWordsList =
                    settingsManager.readStringListSetting(SettingsManager.ARCHIVED_WORDS_LIST)
                val updatedArchivedWordsList = prevArchivedWordsList.filter {
                    settingsState.value.archivedWordsListPreliminary.contains(it)
                }
                settingsManager.saveStringListSetting(
                    SettingsManager.ARCHIVED_WORDS_LIST,
                    updatedArchivedWordsList
                )
                settingsState.value = settingsState.value.copy(
                    archivedWordsList = settingsState.value.archivedWordsListPreliminary,
                    isArchivedWordsDialogShown = false
                )
            }

            is SettingsActions.OnToggleArchivedWordsDialog -> {
                val prevValue = settingsState.value.isArchivedWordsDialogShown
                settingsState.value = settingsState.value.copy(
                    isArchivedWordsDialogShown = !prevValue,
                    archivedWordsListPreliminary = settingsState.value.archivedWordsList
                )
            }

            is SettingsActions.OnUnarchiveWord -> {
                val archivedWordsPreliminary =
                    settingsState.value.archivedWordsListPreliminary.toMutableList()
                settingsState.value = settingsState.value.copy(
                    archivedWordsListPreliminary = archivedWordsPreliminary.filterNot { it == action.word }
                )
            }
        }
    }
}