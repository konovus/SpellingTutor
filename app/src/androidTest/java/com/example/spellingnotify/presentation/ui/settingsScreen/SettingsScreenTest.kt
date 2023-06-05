package com.example.spellingnotify.presentation.ui.settingsScreen

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spellingnotify.core.util.TestTags.SETTING_OPTION_DIALOG
import com.example.spellingnotify.data.di.AppModule
import com.example.spellingnotify.data.notification.FakeNotificationManager
import com.example.spellingnotify.data.preferences.FakeSettingManager
import com.example.spellingnotify.domain.notification.NotificationManager
import com.example.spellingnotify.domain.utils.SettingsManager
import com.example.spellingnotify.navigation.Screens
import com.example.spellingnotify.presentation.ui.MainActivity
import com.example.spellingnotify.presentation.ui.theme.SpellingNotifyTheme
import com.example.spellingnotify.presentation.utils.SettingTimeInterval
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class SettingsScreenTest {

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val scope = CoroutineScope(Dispatchers.Unconfined)


    private lateinit var settingsManager: SettingsManager
    private lateinit var notificationManager: NotificationManager
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        settingsManager = FakeSettingManager()
        notificationManager = FakeNotificationManager()
        viewModel = SettingsViewModel(settingsManager, notificationManager)
        hiltRule.inject()
        composeRule.activity.setContent {
            val navController = rememberNavController()
            SpellingNotifyTheme() {
                NavHost(
                    navController = navController,
                    startDestination = Screens.Settings.route
                ) {
                    composable(route = Screens.Settings.route) {
                        SettingsScreen()
                    }
                }

            }
        }
    }

    @Test
    fun clickLearningIntervalsDialog_isVisible() {
        composeRule.onNodeWithTag(SETTING_OPTION_DIALOG).assertDoesNotExist()
        composeRule.onNodeWithText("Time interval for Learning").performClick()
        composeRule.onNodeWithTag(SETTING_OPTION_DIALOG).assertIsDisplayed()
    }
    @Test
    fun clickExercisingIntervalsDialog_isVisible() {
        composeRule.onNodeWithTag(SETTING_OPTION_DIALOG).assertDoesNotExist()
        composeRule.onNodeWithText("Time interval for Exercising").performClick()
        composeRule.onNodeWithTag(SETTING_OPTION_DIALOG).assertIsDisplayed()
    }
    @Test
    fun clickWordsFilteringDialog_isVisible() {
        composeRule.onNodeWithTag(SETTING_OPTION_DIALOG).assertDoesNotExist()
        composeRule.onNodeWithText("Words filtering options").performClick()
        composeRule.onNodeWithTag(SETTING_OPTION_DIALOG).assertIsDisplayed()
    }
    @Test
    fun clickArchivedWordsDialog_isVisible() {
        composeRule.onNodeWithTag(SETTING_OPTION_DIALOG).assertDoesNotExist()
        composeRule.onNodeWithText("Archived words").performClick()
        composeRule.onNodeWithTag(SETTING_OPTION_DIALOG).assertIsDisplayed()
    }
    @Test
    fun addLearningInterval_clickOnOk_isAdded() {
        composeRule.onNodeWithTag(SETTING_OPTION_DIALOG).assertDoesNotExist()
        composeRule.onNodeWithText("Time interval for Learning").performClick()
        viewModel.onAction(SettingsActions.OnAddLearningInterval(8, 8))
        composeRule.waitForIdle()
        assertThat(viewModel.settingsState.value.learningIntervalsListPreliminary)
            .contains(SettingTimeInterval.create(8, 8))

        composeRule.onNodeWithText("Ok").performClick()
        viewModel.onAction(SettingsActions.OnApplyLearningIntervalsChanges)
        composeRule.waitForIdle()
        assertThat(viewModel.settingsState.value.learningIntervalsList)
            .isEqualTo(viewModel.settingsState.value.learningIntervalsListPreliminary)
    }
    @Test
    fun addExercisingInterval_clickOnOk_isAdded() {
        composeRule.onNodeWithTag(SETTING_OPTION_DIALOG).assertDoesNotExist()
        composeRule.onNodeWithText("Time interval for Exercising").performClick()
        viewModel.onAction(SettingsActions.OnAddExercisingInterval(8, 8))
        composeRule.waitForIdle()
        assertThat(viewModel.settingsState.value.exercisingIntervalsListPreliminary)
            .contains(SettingTimeInterval.create(8, 8))

        composeRule.onNodeWithText("Ok").performClick()
        viewModel.onAction(SettingsActions.OnApplyExercisingIntervalsChanges)
        composeRule.waitForIdle()
        assertThat(viewModel.settingsState.value.exercisingIntervalsList)
            .isEqualTo(viewModel.settingsState.value.exercisingIntervalsListPreliminary)
    }

    @Test
    fun clickOnFiltersDialog_offFilterThreeLetterWords_clickOk_correctFiltersList() {
        composeRule.onNodeWithTag(SETTING_OPTION_DIALOG).assertDoesNotExist()
        composeRule.onNodeWithText("Words filtering options").performClick()
        composeRule.onNodeWithTag(viewModel.settingsState.value.wordsFiltersList.first().text)
            .performClick()
        composeRule.onNodeWithTag(viewModel.settingsState.value.wordsFiltersList.first().text)
            .assertIsOff()
        composeRule.onNodeWithText("Ok").performClick()
        viewModel.onAction(SettingsActions.OnApplyWordsFiltersChanges)
        composeRule.waitForIdle()
        assertThat(viewModel.settingsState.value.wordsFiltersListPreliminary)
            .isEqualTo(viewModel.settingsState.value.wordsFiltersList)

    }


}