package com.example.spellingnotify.presentation.ui.settingsScreen.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeIntervalDialogContent(
    modifier: Modifier = Modifier,
    emptyPlaceholder: String,
    timeIntervalList: List<String>,
    onAddInterval: (Int, Int) -> Unit,
    onEditTimeInterval: (Int, Int, Int) -> Unit,
    onDeleteTimeInterval: (Int) -> Unit,
) {
    val clockState = rememberUseCaseState()
    var intervalIndex = -1


    ClockDialog(
        state = clockState,
        selection = ClockSelection.HoursMinutes(
            onPositiveClick =  { hours, minutes ->
                if (intervalIndex == -1)
                    onAddInterval(hours, minutes)
                else onEditTimeInterval(intervalIndex, hours, minutes)
            }
        )
    )
    if (timeIntervalList.isEmpty())
        Text(
            text = emptyPlaceholder,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
            modifier = Modifier.padding(8.dp)
        )
    Column(
        modifier = modifier
            .animateContentSize(
                tween(300, easing = LinearOutSlowInEasing)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        timeIntervalList.forEachIndexed { index, interval ->
            Row(
                modifier = Modifier.padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = interval,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(6.dp)
                )
                Spacer(modifier = Modifier.width(60.dp))
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    IconButton(onClick = {
                        intervalIndex = index
                        clockState.show()
                    }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { onDeleteTimeInterval(index) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(onClick = { clockState.show() }) {
            Text(text = "Add interval")
        }

    }

}