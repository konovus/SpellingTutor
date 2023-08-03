package com.example.spellingnotify.presentation.ui.settingsScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spellingnotify.core.util.TestTags.SETTING_CUSTOM_OPTION

@Composable
fun SettingsCustomOption(
    modifier: Modifier = Modifier,
    title: String,
    subTitleList: List<String>,
    emptyPlaceholder: String,
    onOptionClick: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onOptionClick() }
            .testTag(SETTING_CUSTOM_OPTION)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSurface,
        )
        if (subTitleList.isEmpty())
            Text(
                text = emptyPlaceholder,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f)
            )
        subTitleList.forEach {
            Text(
                text = it,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f)
            )
        }
    }
}