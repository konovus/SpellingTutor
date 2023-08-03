package com.example.spellingnotify.presentation.ui.settingsScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.spellingnotify.core.util.TestTags.SETTING_OPTION_DIALOG


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsOptionDialog(
    title: String,
    onOk: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
                .testTag(SETTING_OPTION_DIALOG),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 28.dp),
                fontSize = 18.sp
            )
            content()
            Spacer(modifier = Modifier.height(60.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.clickable { onDismiss() }
                )
                Spacer(modifier = Modifier.width(100.dp))
                Text(
                    text = "Ok",
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.clickable { onOk() }
                )
            }
        }
    }
}