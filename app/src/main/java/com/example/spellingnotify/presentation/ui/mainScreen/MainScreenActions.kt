package com.example.spellingnotify.presentation.ui.mainScreen

sealed interface MainScreenActions {
    data class OnSwipeWord(val word: String): MainScreenActions
}