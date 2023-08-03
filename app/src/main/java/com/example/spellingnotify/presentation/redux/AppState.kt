package com.example.spellingnotify.presentation.redux

data class AppState(
    val archivedWordsList: List<String> = listOf(),
    val updateArchivedWords: Boolean = false,
    val wordToGuess: String = "",
    val wordToLearn: String = "",
)
