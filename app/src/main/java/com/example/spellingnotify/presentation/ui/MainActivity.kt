package com.example.spellingnotify.presentation.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.spellingnotify.navigation.Navigation
import com.example.spellingnotify.navigation.Screens
import com.example.spellingnotify.presentation.ui.bottomNavigation.BottomBar
import com.example.spellingnotify.presentation.ui.theme.SpellingNotifyTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class MainActivity : ComponentActivity() {

    private val viewModel: SharedViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter",
        "UnusedMaterialScaffoldPaddingParameter"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window?.statusBarColor = Color.TRANSPARENT
            window?.navigationBarColor = Color.TRANSPARENT
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }
            viewModel.onTopBarTitleChanged(Screens.all[0].title)

            SpellingNotifyTheme {
                Surface {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.primary)
                            .statusBarsPadding()
                            .navigationBarsPadding()
                            .systemBarsPadding(),
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = viewModel.topBarTitle.value,
                                        color = MaterialTheme.colors.onPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                backgroundColor = MaterialTheme.colors.primary
                            )
                        },
                        bottomBar = { BottomBar(navController = navController, viewModel) }
                        ,
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) {
                        Navigation(navController, snackbarHostState)
                    }
                }
            }
        }

    }

}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}
