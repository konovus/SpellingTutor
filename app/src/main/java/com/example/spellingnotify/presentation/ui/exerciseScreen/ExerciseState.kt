package com.example.spellingnotify.presentation.ui.exerciseScreen

import com.example.spellingnotify.domain.models.WordModel

data class ExerciseState(
  val wordTyped: String = "",
  val wordModels: List<WordModel> = listOf(),
  val isCorrect: Boolean? = null,
  val currentIndex: Int = 0,
  val score: Int = 0,
  val total: Int = 1,
  val streak: Int = 0,
  val isGameRunning: Boolean = false,
  val isEndGame: Boolean = false,
  val isScoreBoardShown: Boolean = false,
  val scoreBoardList: List<String> = listOf()
)