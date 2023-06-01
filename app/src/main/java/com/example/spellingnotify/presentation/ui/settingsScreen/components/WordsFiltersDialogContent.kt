package com.example.spellingnotify.presentation.ui.settingsScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spellingnotify.domain.filters.WordsFilter

@Composable
fun WordsFiltersDialogContent(
    modifier: Modifier = Modifier,
    allFilters: List<WordsFilter>,
    onToggleFilter: (Int) -> Unit
) {

    Column(
        modifier = modifier.widthIn(max = 300.dp)
    ) {

        allFilters.forEachIndexed { index, wordsFilter ->
            Row(
                modifier = Modifier.padding(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = wordsFilter.text,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Left
                )
                Switch(
                    checked = wordsFilter.value,
                    onCheckedChange = { onToggleFilter(index) }
                )
            }
        }
    }
}