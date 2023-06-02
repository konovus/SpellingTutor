package com.example.spellingnotify.presentation.ui.settingsScreen

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spellingnotify.core.util.TestTags.DIALOG_WRAPPER
import com.example.spellingnotify.data.di.AppModule
import com.example.spellingnotify.navigation.Screens
import com.example.spellingnotify.presentation.ui.MainActivity
import com.example.spellingnotify.presentation.ui.theme.SpellingNotifyTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class SettingsScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
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
        composeRule.onNodeWithTag(DIALOG_WRAPPER).assertDoesNotExist()
        composeRule.onNodeWithText("Time interval for Learning").performClick()
        composeRule.onNodeWithTag(DIALOG_WRAPPER).assertIsDisplayed()
    }


}