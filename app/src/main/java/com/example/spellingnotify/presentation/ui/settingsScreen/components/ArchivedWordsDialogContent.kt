package com.example.spellingnotify.presentation.ui.settingsScreen.components

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.LocalMinimumTouchTargetEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spellingnotify.data.di.TAG
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ArchivedWordsDialogContent(
    modifier: Modifier = Modifier,
    emptyPlaceholder: String,
    archivedWords: List<String>,
    onUnArchiveWord: (String) -> Unit,
) {

    if (archivedWords.isEmpty())
        Text(
            text = emptyPlaceholder,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
            modifier = Modifier.padding(8.dp)
        )

    LazyColumn(
        modifier = Modifier.heightIn(max = 300.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(
            items = archivedWords,
            key = { word -> word}
        ) { word ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(4.dp)
                    .animateItemPlacement(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = word,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(6.dp)
                        .weight(1f),
                    textAlign = TextAlign.Left
                )
                Spacer(modifier = Modifier.width(60.dp))
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    IconButton(onClick = {
                        onUnArchiveWord(word)
                    }) {
                        Icon(imageVector = Icons.Default.Unarchive, contentDescription = "Unarchive")
                    }
                }

        }
        }
    }

}