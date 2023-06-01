package com.example.spellingnotify.presentation.ui.settingsScreen

sealed interface SettingsActions {

    object OnApplyArchivedWordsChanges: SettingsActions
    object OnToggleArchivedWordsDialog: SettingsActions

    object OnApplyLearningIntervalsChanges: SettingsActions
    object OnToggleLearningIntervalsDialog: SettingsActions

    object OnApplyExercisingIntervalsChanges: SettingsActions
    object OnToggleExercisingIntervalsDialog: SettingsActions

    object OnApplyWordsFiltersChanges: SettingsActions
    object OnToggleWordsFilterDialog: SettingsActions

    data class OnUnarchiveWord(val word: String): SettingsActions

    data class OnEditLearningInterval(val index: Int, val hours: Int, val minutes: Int): SettingsActions
    data class OnDeleteLearningInterval(val index: Int): SettingsActions
    data class OnAddLearningInterval(val hours: Int, val minutes: Int): SettingsActions

    data class OnEditExercisingInterval(val index: Int, val hours: Int, val minutes: Int): SettingsActions
    data class OnDeleteExercisingInterval(val index: Int): SettingsActions
    data class OnAddExercisingInterval(val hours: Int, val minutes: Int): SettingsActions

    data class OnToggleWordsFilter(val index: Int): SettingsActions

    data class OnPermissionRequiredSettingClick(val setting: PermissionRequiredSetting): SettingsActions
}
