package com.example.spellingnotify.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChecklistRtl
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController


sealed class Screens(
    val route: String,
    val title: String,
    val icon: ImageVector
    ) {
    object Main : Screens("main_screen", "Main", Icons.Rounded.List)
    object Settings : Screens("settings_screen", "Settings", Icons.Rounded.Settings)
    object Exercise : Screens("exercise_screen", "Exercise", Icons.Rounded.ChecklistRtl)

    companion object {
        val all = listOf(
            Main,Exercise,Settings
        )
    }
}


class Actions(navController: NavHostController) {
    val navigateToExercise: () -> Unit = {
        navController.navigate(Screens.Exercise.route)
    }
    val navigateToSettings: () -> Unit = {
        navController.navigate(Screens.Settings.route)
    }
    val navigateBack: () -> Unit = {
        navController.popBackStack()
    }
}