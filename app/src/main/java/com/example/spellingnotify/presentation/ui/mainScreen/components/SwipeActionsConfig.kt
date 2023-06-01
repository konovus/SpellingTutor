package com.example.spellingnotify.presentation.ui.mainScreen.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class SwipeActionsConfig(
    val threshold: Float,
    val icon: ImageVector,
    val iconTint: Color,
    val background: Color,
    val stayDismissed: Boolean,
    val onDismiss: () -> Unit,  
)