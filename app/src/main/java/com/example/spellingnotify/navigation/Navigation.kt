package com.example.spellingnotify.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.spellingnotify.presentation.ui.exerciseScreen.ExerciseScreen
import com.example.spellingnotify.presentation.ui.mainScreen.MainScreen
import com.example.spellingnotify.presentation.ui.settingsScreen.SettingsScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val actions = remember(navController) { Actions(navController) }

    NavHost(
        navController = navController,
        startDestination = Screens.Main.route
    ) {

        composable(
            route = Screens.Main.route,
        ) {
            MainScreen(snackbarHostState = snackbarHostState)
        }

        composable(
            route = Screens.Settings.route,
        ) {
            SettingsScreen()
        }

        composable(
            route = Screens.Exercise.route
        ) {
            ExerciseScreen()
        }

    }
}
