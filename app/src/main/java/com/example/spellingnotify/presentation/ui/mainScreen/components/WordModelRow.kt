package com.example.spellingnotify.presentation.ui.mainScreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spellingnotify.domain.models.WordModel

@Composable
fun WordModelRow(
    modifier: Modifier = Modifier,
    index: Int,
    bgColors: List<Color>,
    onWordClick: (String) -> Unit,
    wordModel: WordModel,
    isLoading: Boolean,
    isOpen: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(0.7f)
            .border(3.dp, bgColors[index].copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(3.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(bgColors[index])
            .animateContentSize(tween(300))
            .clickable { onWordClick(wordModel.word) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = wordModel.word,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        AnimatedVisibility(visible = isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(6.dp))
        }
        AnimatedVisibility(isOpen) {
            Text(
                text = wordModel.definition,
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(6.dp)
            )
        }
    }
}
