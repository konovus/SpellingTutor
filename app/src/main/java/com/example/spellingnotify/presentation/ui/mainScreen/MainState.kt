package com.example.spellingnotify.presentation.ui.mainScreen

import androidx.compose.ui.graphics.Color
import com.example.spellingnotify.data.local.WordsData
import com.example.spellingnotify.domain.filters.WordsFilter
import com.example.spellingnotify.domain.models.WordModel

data class MainState(
    val wordModels: List<WordModel> = listOf(),
    val allFilters: List<WordsFilter> = listOf(),
    val bgColors: List<Color> = List(WordsData.allWords.size) {
        Color(
            red = (0..255).random(),
            green = (150..155).random(),
            blue = (70..200).random()
        )
    }
)

