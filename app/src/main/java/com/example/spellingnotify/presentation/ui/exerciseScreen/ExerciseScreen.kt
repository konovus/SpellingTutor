package com.example.spellingnotify.presentation.ui.exerciseScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spellingnotify.presentation.ui.exerciseScreen.components.ScoreBoardDialog
import com.example.spellingnotify.presentation.ui.exerciseScreen.components.Timer
import com.example.spellingnotify.presentation.ui.theme.Error
import com.example.spellingnotify.presentation.ui.theme.Success
import com.example.spellingnotify.presentation.utils.distinctChars
import com.example.spellingnotify.presentation.utils.observeWithLifecycle


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ExerciseScreen(
    modifier: Modifier = Modifier,
    viewModel: ExerciseViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val isKeyboardOpen by keyboardAsState()
    val focusRequest = remember { FocusRequester() }
    val currentIndex = viewModel.state.value.currentIndex
    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.event.observeWithLifecycle {
        snackbarHostState.showSnackbar(message = it)
    }

    LaunchedEffect(key1 = true, key2 = state.isGameRunning) {
        if (state.isGameRunning)
            focusRequest.requestFocus()
    }

    if (state.isScoreBoardShown)
        showScoreBoard(viewModel, state)

    Column(
        modifier = modifier
            .padding(top = 20.dp, bottom = 76.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (!state.isGameRunning)
            Arrangement.Center else Arrangement.Top
    ) {

        if (state.isEndGame)
            ShowEndGameScreen(state, viewModel)

        if (!state.isGameRunning && !state.isEndGame)
            ShowStartGameScreen(viewModel)

        if (state.isGameRunning && !state.isEndGame)
            ShowGameIsRunningScreen(
                state = state,
                viewModel = viewModel,
                isKeyboardOpen = isKeyboardOpen,
                currentIndex = currentIndex,
                focusRequest = focusRequest
            )
    }
}

@Composable
private fun ShowEndGameScreen(
    state: ExerciseState,
    viewModel: ExerciseViewModel
) {
    Text(
        text = "Your score:",
        fontSize = 28.sp
    )
    Spacer(modifier = Modifier.height(40.dp))
    Text(
        text = "${state.score}",
        fontSize = 24.sp
    )
    Spacer(modifier = Modifier.height(60.dp))
    Button(onClick = { viewModel.startGame() }) {
        Text(
            text = "Try again",
            fontSize = 24.sp
        )
    }
    Spacer(modifier = Modifier.height(26.dp))
    Text(
        text = "ScoreBoard",
        fontSize = 20.sp,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable {
            viewModel.toggleScoreBoard()
        }
    )
}

@Composable
private fun ShowStartGameScreen(viewModel: ExerciseViewModel) {
    Button(onClick = {
        viewModel.startGame()
    }) {
        Text(
            text = "Start",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
    Spacer(modifier = Modifier.height(26.dp))
    Text(
        text = "ScoreBoard",
        fontSize = 20.sp,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable {
            viewModel.toggleScoreBoard()
        }
    )
}

@Composable
private fun ShowGameIsRunningScreen(
    state: ExerciseState,
    viewModel: ExerciseViewModel,
    isKeyboardOpen: Boolean,
    currentIndex: Int,
    focusRequest: FocusRequester
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Score",
                fontSize = 20.sp
            )
            Text(
                text = "${state.score}",
                fontSize = 18.sp
            )
        }
        Timer(
            totalTime = 60L * 1000L,
            inactiveBarColor = Color.DarkGray,
            activeBarColor = MaterialTheme.colors.primary,
            modifier = Modifier.size(60.dp),
            viewModel = viewModel
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Streak",
                fontSize = 20.sp
            )
            Text(
                text = "${state.streak}",
                fontSize = 18.sp
            )
        }
    }
    Spacer(
        modifier = Modifier
            .height(
                if (state.isGameRunning || state.isEndGame)
                    20.dp else 280.dp
            )
    )
    AnimatedVisibility(visible = !isKeyboardOpen) {
        Spacer(modifier = Modifier.height(70.dp))
    }
    Text(
        text = "Type the word:",
        fontSize = 32.sp,
    )
    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = "\"${state.wordModels[currentIndex].word.distinctChars()}\"",
        fontSize = 26.sp
    )
    Spacer(modifier = Modifier.height(20.dp))
    OutlinedTextField(
        visualTransformation = VisualTransformation.None,
        value = state.wordTyped,
        onValueChange = {
            viewModel.evaluateWord(it)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = if (state.isCorrect == null)
                MaterialTheme.colors.primary
            else if (state.isCorrect)
                Success
            else Error,
        ),
        label = {
            Text(text = "Type word")
        },
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            autoCorrect = false,
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(onNext = {
            viewModel.nextWord()
        }),
        maxLines = 1,
        singleLine = true,
        modifier = Modifier.focusRequester(focusRequest)
    )
    Spacer(modifier = Modifier.height(60.dp))
    Button(
        onClick = { viewModel.nextWord() }) {
        Text(
            text = "Next word",
            fontSize = 22.sp,
        )
    }
}


@Composable
private fun showScoreBoard(
    viewModel: ExerciseViewModel,
    state: ExerciseState
) {
    ScoreBoardDialog(
        title = "Score Board",
        onOk = { viewModel.toggleScoreBoard() },
        onDismiss = { viewModel.toggleScoreBoard() }
    ) {
        if (state.scoreBoardList.isEmpty())
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "No scores yet",
                fontSize = 22.sp,
            )
        else {
            state.scoreBoardList.forEachIndexed { index, score ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (index == 9)
                            "${index + 1})  ${score.split(" ")[0]}"
                        else "  ${index + 1})  ${score.split(" ")[0]}",
                        fontSize = 20.sp
                    )
                    Text(
                        text = score.split(" ")[1],
                        fontSize = 20.sp
                    )
                }
            }
            if (state.scoreBoardList.size < 10)
                repeat(10 - state.scoreBoardList.size) {
                    val index = it + state.scoreBoardList.size
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (index == 9)
                                "${index + 1})  ..........." else "  ${index + 1})  ...........",
                            fontSize = 20.sp
                        )
                        Text(
                            text = "......",
                            fontSize = 20.sp
                        )
                    }
                }

        }

    }
}

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

@Preview(showBackground = true)
@Composable
fun ExerciseScreenPreview() {
    ExerciseScreen()
}