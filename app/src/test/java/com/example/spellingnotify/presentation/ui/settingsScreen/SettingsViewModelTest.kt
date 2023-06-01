package com.example.spellingnotify.presentation.ui.settingsScreen

import com.MainDispatcherRule
import com.example.spellingnotify.data.notification.FakeNotificationManager
import com.example.spellingnotify.data.preferences.FakeSettingManager
import com.example.spellingnotify.domain.filters.WordsFilter
import com.example.spellingnotify.domain.notification.NotificationManager
import com.example.spellingnotify.domain.utils.SettingsManager
import com.example.spellingnotify.presentation.utils.SettingTimeInterval
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var settingsManager: SettingsManager
    private lateinit var notificationManager: NotificationManager
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        settingsManager = FakeSettingManager()
        notificationManager = FakeNotificationManager()
        viewModel = SettingsViewModel(settingsManager, notificationManager)
    }

    @Test
    fun `onAction OnToggleLearningIntervalsDialog, isShown != prev isShown, preliminaryList == finalList`() {
        val interval1 = SettingTimeInterval.create(10, 10)
        val interval2 = SettingTimeInterval.create(20, 10)
        val intervals = listOf(interval1, interval2)
        viewModel.settingsState.value = viewModel.settingsState.value.copy(
            learningIntervalsListPreliminary = intervals,
            learningIntervalsList = intervals
        )
        viewModel.onAction(SettingsActions.OnEditLearningInterval(0, 5, 5))
        val prevIsShown = viewModel.settingsState.value.isLearningIntervalsDialogShown
        viewModel.onAction(SettingsActions.OnToggleLearningIntervalsDialog)
        assertThat(prevIsShown).isNotEqualTo(viewModel.settingsState.value.isLearningIntervalsDialogShown)
        assertThat(viewModel.settingsState.value.learningIntervalsListPreliminary).isEqualTo(
            viewModel.settingsState.value.learningIntervalsList
        )
    }

    @Test
    fun `onAction OnEditLearningInterval, preliminaryList has the edited interval, but finalList doesn't`() {
        val interval1 = SettingTimeInterval.create(10, 10)
        val interval2 = SettingTimeInterval.create(20, 10)
        val intervals = listOf(interval1, interval2)
        viewModel.settingsState.value = viewModel.settingsState.value.copy(
                learningIntervalsListPreliminary = intervals,
                learningIntervalsList = intervals
            )
        viewModel.onAction(SettingsActions.OnEditLearningInterval(0, 5, 5))
        assertThat(viewModel.settingsState.value.learningIntervalsListPreliminary).doesNotContain(interval1)
        assertThat(viewModel.settingsState.value.learningIntervalsListPreliminary).contains(
            SettingTimeInterval.create(5, 5)
        )
        assertThat(viewModel.settingsState.value.learningIntervalsList).contains(interval1)
        assertThat(viewModel.settingsState.value.learningIntervalsList).doesNotContain(
            SettingTimeInterval.create(5, 5)
        )
    }

    @Test
    fun `onAction OnAddLearningInterval, preliminaryList has the added interval, but finalList doesn't`() {
        viewModel.onAction(SettingsActions.OnAddLearningInterval( 5, 5))
        assertThat(viewModel.settingsState.value.learningIntervalsListPreliminary).contains(
            SettingTimeInterval.create(5, 5)
        )
        assertThat(viewModel.settingsState.value.learningIntervalsList).doesNotContain(
            SettingTimeInterval.create(5, 5)
        )
    }

    @Test
    fun `onAction OnDeleteLearningInterval, preliminaryList does not contain the deleted interval, but finalList contains`() {
        val interval1 = SettingTimeInterval.create(10, 10)
        val interval2 = SettingTimeInterval.create(20, 10)
        val intervals = listOf(interval1, interval2)
        viewModel.settingsState.value = viewModel.settingsState.value.copy(
                learningIntervalsListPreliminary = intervals,
                learningIntervalsList = intervals
        )
        viewModel.onAction(SettingsActions.OnDeleteLearningInterval(0))
        assertThat(viewModel.settingsState.value.learningIntervalsListPreliminary).doesNotContain(
            SettingTimeInterval.create(10, 10)
        )
        assertThat(viewModel.settingsState.value.learningIntervalsList).contains(
            SettingTimeInterval.create(10, 10)
        )
    }

    @Test
    fun `onAction OnToggleWordsFilterDialog, assert that isShown != prev isShown, preliminaryList == finalList`() {
        viewModel.onAction(SettingsActions.OnToggleWordsFilter(2))

        val prevIsShown = viewModel.settingsState.value.isWordsFilterDialogShown
        viewModel.onAction(SettingsActions.OnToggleWordsFilterDialog)
        assertThat(prevIsShown).isNotEqualTo(viewModel.settingsState.value.isWordsFilterDialogShown)
        assertThat(viewModel.settingsState.value.wordsFiltersListPreliminary).isEqualTo(
            viewModel.settingsState.value.wordsFiltersList
        )
    }

    @Test
    fun `onAction OnApplyWordsFiltersChanges, finalList == preliminaryList and isShown == false`() {
        viewModel.onAction(SettingsActions.OnToggleWordsFilter(2))

        viewModel.onAction(SettingsActions.OnApplyWordsFiltersChanges)
        assertThat(viewModel.settingsState.value.isWordsFilterDialogShown).isFalse()
        assertThat(viewModel.settingsState.value.wordsFiltersList).isEqualTo(
            viewModel.settingsState.value.wordsFiltersListPreliminary
        )
    }

    @Test
    fun `onAction OnToggleWordsFilter, filter updated in preliminaryList`() {
        viewModel.settingsState.value = viewModel.settingsState.value.copy(
            wordsFiltersListPreliminary = WordsFilter.allFilters.map {
                WordsFilter(it.text, true)
            }
        )
        viewModel.onAction(SettingsActions.OnToggleWordsFilter(2))
        assertThat(viewModel.settingsState.value.wordsFiltersListPreliminary[2].value).isFalse()
    }

    @Test
    fun `onAction OnAddExercisingInterval, interval is added`() {
        val interval = SettingTimeInterval.create(10, 10)
        assertThat(viewModel.settingsState.value.exercisingIntervalsListPreliminary)
            .doesNotContain(interval)
        viewModel.onAction(SettingsActions.OnAddExercisingInterval(10, 10))
        assertThat(viewModel.settingsState.value.exercisingIntervalsListPreliminary)
            .contains(interval)
    }

    @Test
    fun `on Action OnDeleteExercisingInterval, interval is deleted`() {
        val interval = SettingTimeInterval.create(10, 10)
        viewModel.onAction(SettingsActions.OnAddExercisingInterval(10, 10))
        assertThat(viewModel.settingsState.value.exercisingIntervalsListPreliminary)
            .contains(interval)
        viewModel.onAction(SettingsActions.OnDeleteExercisingInterval(0))
        assertThat(viewModel.settingsState.value.exercisingIntervalsListPreliminary)
            .doesNotContain(interval)
    }

    @Test
    fun `onAction OnEditExercisingInterval, preliminaryList has the edited interval, but finalList doesn't`() {
        val interval1 = SettingTimeInterval.create(10, 10)
        val interval2 = SettingTimeInterval.create(20, 10)
        val intervals = listOf(interval1, interval2)
        viewModel.settingsState.value = viewModel.settingsState.value.copy(
            exercisingIntervalsListPreliminary = intervals,
            exercisingIntervalsList = intervals
        )
        viewModel.onAction(SettingsActions.OnEditExercisingInterval(0, 5, 5))
        assertThat(viewModel.settingsState.value.exercisingIntervalsListPreliminary).doesNotContain(interval1)
        assertThat(viewModel.settingsState.value.exercisingIntervalsListPreliminary).contains(
            SettingTimeInterval.create(5, 5)
        )
        assertThat(viewModel.settingsState.value.exercisingIntervalsList).contains(interval1)
        assertThat(viewModel.settingsState.value.exercisingIntervalsList).doesNotContain(
            SettingTimeInterval.create(5, 5)
        )
    }

    @Test
    fun `onAction OnToggleExercisingIntervalsDialog, isShown != prev isShown, preliminaryList == finalList`() {
        val interval1 = SettingTimeInterval.create(10, 10)
        val interval2 = SettingTimeInterval.create(20, 10)
        val intervals = listOf(interval1, interval2)
        viewModel.settingsState.value = viewModel.settingsState.value.copy(
            exercisingIntervalsListPreliminary = intervals,
            exercisingIntervalsList = intervals
        )
        viewModel.onAction(SettingsActions.OnEditExercisingInterval(0, 5, 5))
        val prevIsShown = viewModel.settingsState.value.isExercisingIntervalsDialogShown
        viewModel.onAction(SettingsActions.OnToggleExercisingIntervalsDialog)
        assertThat(prevIsShown).isNotEqualTo(viewModel.settingsState.value.isExercisingIntervalsDialogShown)
        assertThat(viewModel.settingsState.value.exercisingIntervalsListPreliminary).isEqualTo(
            viewModel.settingsState.value.exercisingIntervalsList
        )
    }

    @Test
    fun `onAction OnPermissionRequiredSettingClick, permissionRequiredSetting is set`() {
        viewModel.onAction(SettingsActions.OnPermissionRequiredSettingClick(PermissionRequiredSetting.EXERCISING))
        assertThat(viewModel.settingsState.value.permissionRequiredSetting).isEqualTo(
            PermissionRequiredSetting.EXERCISING
        )
    }

    @Test
    fun `onAction OnApplyArchivedWordsChanges, finalList == preliminaryList, isShow == false`() {
        val archivedWords = listOf("bubble", "parrot", "carrot")
        viewModel.settingsState.value = viewModel.settingsState.value.copy(
            archivedWordsListPreliminary = archivedWords,
            isArchivedWordsDialogShown = true
        )
        viewModel.onAction(SettingsActions.OnApplyArchivedWordsChanges)
        assertThat(viewModel.settingsState.value.archivedWordsList)
            .isEqualTo(viewModel.settingsState.value.archivedWordsListPreliminary)
        assertThat(viewModel.settingsState.value.isArchivedWordsDialogShown).isFalse()
    }

    @Test
    fun `onAction OnToggleArchivedWordsDialog, isShown != prev isShown, finalList == preliminaryList`() {
        val archivedWords = listOf("bubble", "parrot", "carrot")
        viewModel.settingsState.value = viewModel.settingsState.value.copy(
            archivedWordsListPreliminary = archivedWords,
            isArchivedWordsDialogShown = true
        )
        viewModel.onAction(SettingsActions.OnToggleArchivedWordsDialog)
        assertThat(viewModel.settingsState.value.archivedWordsList)
            .isEqualTo(viewModel.settingsState.value.archivedWordsListPreliminary)
        assertThat(viewModel.settingsState.value.isArchivedWordsDialogShown).isFalse()
    }

    @Test
    fun `onAction OnUnarchiveWord, word is unarchived`() {
        val archivedWords = listOf("bubble", "parrot", "carrot")
        viewModel.settingsState.value = viewModel.settingsState.value.copy(
            archivedWordsListPreliminary = archivedWords,
        )
        viewModel.onAction(SettingsActions.OnUnarchiveWord("bubble"))
        assertThat(viewModel.settingsState.value.archivedWordsListPreliminary)
            .doesNotContain("bubble")
    }

}