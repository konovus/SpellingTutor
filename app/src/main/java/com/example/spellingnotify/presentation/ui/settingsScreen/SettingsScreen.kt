package com.example.spellingnotify.presentation.ui.settingsScreen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spellingnotify.R
import com.example.spellingnotify.domain.filters.WordsFilter
import com.example.spellingnotify.presentation.ui.settingsScreen.components.NOTIFICATION_PERMISSION
import com.example.spellingnotify.presentation.ui.settingsScreen.components.PermissionDialog
import com.example.spellingnotify.presentation.ui.settingsScreen.components.isPermissionGranted
import com.example.spellingnotify.presentation.ui.settingsScreen.components.permissionLauncher
import com.example.spellingnotify.presentation.ui.openAppSettings
import com.example.spellingnotify.presentation.ui.settingsScreen.components.ArchivedWordsDialogContent
import com.example.spellingnotify.presentation.ui.settingsScreen.components.SettingsCustomOption
import com.example.spellingnotify.presentation.ui.settingsScreen.components.SettingsOptionDialog
import com.example.spellingnotify.presentation.ui.settingsScreen.components.TimeIntervalDialogContent
import com.example.spellingnotify.presentation.ui.settingsScreen.components.WordsFiltersDialogContent

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var shouldShowPermissionDialog by remember { mutableStateOf(false) }
    val permissionLauncher = permissionLauncher { isGranted ->
        if (isGranted)
            when (viewModel.settingsState.value.permissionRequiredSetting) {
                PermissionRequiredSetting.LEARNING -> viewModel.onAction(SettingsActions.OnToggleLearningIntervalsDialog)
                PermissionRequiredSetting.EXERCISING -> viewModel.onAction(SettingsActions.OnToggleExercisingIntervalsDialog)
                null -> return@permissionLauncher
            }
        else shouldShowPermissionDialog = true
    }

    if (shouldShowPermissionDialog)
        ShowPermissionDialog(context, shouldShowPermissionDialog, permissionLauncher)

    if (viewModel.settingsState.value.isLearningIntervalsDialogShown)
        ShowLearningIntervalsDialog(viewModel)

    if (viewModel.settingsState.value.isExercisingIntervalsDialogShown)
        ShowExercisingIntervalsDialog(viewModel)

    if (viewModel.settingsState.value.isWordsFilterDialogShown)
        ShowWordsFilterDialog(viewModel)

    if (viewModel.settingsState.value.isArchivedWordsDialogShown)
        ShowArchivedWordsDialog(viewModel)

    Column {
        Spacer(modifier = Modifier.height(96.dp))
        SettingsCustomOption(
            title = "Time interval for Learning",
            subTitleList = viewModel.settingsState.value.learningIntervalsList,
            emptyPlaceholder = "No intervals set",
            onOptionClick = {
                if (isPermissionGranted(context, NOTIFICATION_PERMISSION))
                    viewModel.onAction(SettingsActions.OnToggleLearningIntervalsDialog)
                else {
                    viewModel.onAction(
                        SettingsActions.OnPermissionRequiredSettingClick(
                            PermissionRequiredSetting.LEARNING
                        )
                    )
                    permissionLauncher.launch(NOTIFICATION_PERMISSION)
                }
            }
        )
        Divider(Modifier.padding(vertical = 30.dp))
        SettingsCustomOption(
            title = "Time interval for Exercising",
            subTitleList = viewModel.settingsState.value.exercisingIntervalsList,
            emptyPlaceholder = "No intervals set",
            onOptionClick = {
                if (isPermissionGranted(context, NOTIFICATION_PERMISSION))
                    viewModel.onAction(SettingsActions.OnToggleExercisingIntervalsDialog)
                else {
                    viewModel.onAction(
                        SettingsActions.OnPermissionRequiredSettingClick(
                            PermissionRequiredSetting.EXERCISING
                        )
                    )
                    permissionLauncher.launch(NOTIFICATION_PERMISSION)
                }
            }
        )
        Divider(Modifier.padding(vertical = 30.dp))
        SettingsCustomOption(
            title = "Words filtering options",
            subTitleList = if (viewModel.settingsState.value.wordsFiltersList.filter { it.value }.size == WordsFilter.allFilters.size)
                listOf("All filters") else viewModel.settingsState.value.wordsFiltersList.filter { it.value }
                .map { it.text },
            emptyPlaceholder = "No filters set",
            onOptionClick = { viewModel.onAction(SettingsActions.OnToggleWordsFilterDialog) }
        )
        Divider(Modifier.padding(vertical = 30.dp))
        SettingsCustomOption(
            title = "Archived words list",
            subTitleList =
            if (viewModel.settingsState.value.archivedWordsList.size == 1)
                listOf(viewModel.settingsState.value.archivedWordsList.first())
            else listOf("${viewModel.settingsState.value.archivedWordsList.size} words archived"),
            emptyPlaceholder = "No archived words",
            onOptionClick = { viewModel.onAction(SettingsActions.OnToggleArchivedWordsDialog) }
        )
        Divider(Modifier.padding(vertical = 30.dp))

    }
}

@Composable
private fun ShowArchivedWordsDialog(viewModel: SettingsViewModel) {
    SettingsOptionDialog(
        title = "Archived words",
        onOk = { viewModel.onAction(SettingsActions.OnApplyArchivedWordsChanges) },
        onDismiss = { viewModel.onAction(SettingsActions.OnToggleArchivedWordsDialog) }
    ) {
        ArchivedWordsDialogContent(
            archivedWords = viewModel.settingsState.value.archivedWordsListPreliminary,
            onUnArchiveWord = { word -> viewModel.onAction(SettingsActions.OnUnarchiveWord(word)) },
            emptyPlaceholder = "No archived words"
        )
    }
}

@Composable
private fun ShowWordsFilterDialog(viewModel: SettingsViewModel) {
    SettingsOptionDialog(
        title = "Words filters",
        onOk = { viewModel.onAction(SettingsActions.OnApplyWordsFiltersChanges) },
        onDismiss = { viewModel.onAction(SettingsActions.OnToggleWordsFilterDialog) }
    ) {
        WordsFiltersDialogContent(
            allFilters = viewModel.settingsState.value.wordsFiltersListPreliminary,
            onToggleFilter = { index ->
                viewModel.onAction(
                    SettingsActions.OnToggleWordsFilter(
                        index
                    )
                )
            }
        )
    }
}

@Composable
private fun ShowExercisingIntervalsDialog(viewModel: SettingsViewModel) {
    SettingsOptionDialog(
        title = "Notifications intervals",
        onDismiss = { viewModel.onAction(SettingsActions.OnToggleExercisingIntervalsDialog) },
        onOk = { viewModel.onAction(SettingsActions.OnApplyExercisingIntervalsChanges) },
        content = {
            TimeIntervalDialogContent(
                emptyPlaceholder = "No intervals set",
                timeIntervalList = viewModel.settingsState.value.exercisingIntervalsListPreliminary,
                onAddInterval = { hours, minutes ->
                    viewModel.onAction(SettingsActions.OnAddExercisingInterval(hours, minutes))
                },
                onEditTimeInterval = { index, hours, minutes ->
                    viewModel.onAction(
                        SettingsActions.OnEditExercisingInterval(
                            index,
                            hours,
                            minutes
                        )
                    )
                },
                onDeleteTimeInterval = { index ->
                    viewModel.onAction(
                        SettingsActions.OnDeleteExercisingInterval(
                            index
                        )
                    )
                }
            )
        }
    )
}

@Composable
private fun ShowLearningIntervalsDialog(viewModel: SettingsViewModel) {
    SettingsOptionDialog(
        title = "Notifications intervals",
        onDismiss = { viewModel.onAction(SettingsActions.OnToggleLearningIntervalsDialog) },
        onOk = { viewModel.onAction(SettingsActions.OnApplyLearningIntervalsChanges) },
        content = {
            TimeIntervalDialogContent(
                emptyPlaceholder = "No intervals set",
                timeIntervalList = viewModel.settingsState.value.learningIntervalsListPreliminary,
                onAddInterval = { hours, minutes ->
                    viewModel.onAction(SettingsActions.OnAddLearningInterval(hours, minutes))
                },
                onEditTimeInterval = { index, hours, minutes ->
                    viewModel.onAction(
                        SettingsActions.OnEditLearningInterval(
                            index,
                            hours,
                            minutes
                        )
                    )
                },
                onDeleteTimeInterval = { index ->
                    viewModel.onAction(
                        SettingsActions.OnDeleteLearningInterval(
                            index
                        )
                    )
                }
            )
        }
    )
}

@Composable
private fun ShowPermissionDialog(
    context: Context,
    shouldShowPermissionDialog: Boolean,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>
) {
    var shouldShowPermissionDialog1 = shouldShowPermissionDialog
    PermissionDialog(
        description = stringResource(R.string.post_notifications_permission_description),
        permanentlyDeclinedDescription = stringResource(R.string.post_notifications_permanently_declined_description),
        isPermanentlyDeclined = !(context as Activity).shouldShowRequestPermissionRationale(
            NOTIFICATION_PERMISSION
        ),
        onDismiss = { shouldShowPermissionDialog1 = false },
        onOkClick = {
            permissionLauncher.launch(NOTIFICATION_PERMISSION)
            shouldShowPermissionDialog1 = false
        },
        onGoToAppSettingsClick = {
            shouldShowPermissionDialog1 = false
            context.openAppSettings()
        }
    )
}

enum class PermissionRequiredSetting {
    LEARNING, EXERCISING
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
    )
}