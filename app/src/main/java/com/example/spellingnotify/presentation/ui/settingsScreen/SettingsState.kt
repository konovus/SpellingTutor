package com.example.spellingnotify.presentation.ui.settingsScreen

import androidx.compose.runtime.toMutableStateList
import com.example.spellingnotify.domain.filters.WordsFilter

data class SettingsState(
    val permissionRequiredSetting: PermissionRequiredSetting? = null,
    val learningIntervalsPermissionChecker: Boolean = false,
    val exercisingIntervalsPermissionChecker: Boolean = false,
    val archivedWordsList: List<String> = listOf(),
    val archivedWordsListPreliminary: List<String> = listOf(),
    val learningIntervalsList: List<String> = listOf(),
    val learningIntervalsListPreliminary: List<String> = listOf(),
    val exercisingIntervalsList: List<String> = listOf(),
    val exercisingIntervalsListPreliminary: List<String> = listOf(),
    val wordsFiltersList: List<WordsFilter> = WordsFilter.allFilters,
    val wordsFiltersListPreliminary: List<WordsFilter> = WordsFilter.allFilters.toMutableStateList(),
    val isNumberOfNotificationsPerDayDialogShown: Boolean = false,
    val isLearningIntervalsDialogShown: Boolean = false,
    val isExercisingIntervalsDialogShown: Boolean = false,
    val isWordsFilterDialogShown: Boolean = false,
    val isArchivedWordsDialogShown: Boolean = false,
)