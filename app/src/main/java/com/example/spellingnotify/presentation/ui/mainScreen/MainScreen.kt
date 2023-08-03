package com.example.spellingnotify.presentation.ui.mainScreen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.view.MotionEvent.ACTION_DOWN
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spellingnotify.presentation.ui.mainScreen.components.SwipeBackground
import com.example.spellingnotify.presentation.ui.mainScreen.components.WordModelRow
import com.example.spellingnotify.presentation.utils.observeWithLifecycle

@OptIn( ExperimentalFoundationApi::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    viewModel: MainViewModel = hiltViewModel()
) {
    viewModel.event.observeWithLifecycle {
        snackbarHostState.showSnackbar(message = it)
    }
    val mainListState = rememberLazyListState()
    LaunchedEffect(key1 = true) {
        viewModel.filterList()
    }
    val scope = rememberCoroutineScope()
    val touchPositionsX = mutableListOf<Float>()
    val touchPositionsY = mutableListOf<Float>()

    LazyColumn(
        state = mainListState,
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 72.dp),

        ) {
        itemsIndexed(
            items = viewModel.state.value.wordModels,
            key = { _, wordModel -> wordModel.word }
        ) { index, wordModel ->
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    when (it) {
                        DismissValue.DismissedToEnd -> {
                            if (touchPositionsX.last() - touchPositionsX.first() > 450 &&
                                    touchPositionsY.last() - touchPositionsY.first() < 20) {
                                viewModel.onEvent(MainScreenActions.OnSwipeWord(wordModel.word))
                                true
                            } else false
                        }
                        else -> false
                    }
                }
            )
            SwipeToDismiss(
                dismissThresholds = {
                    FractionalThreshold(0.40f)
                },
                state = dismissState,
                modifier = Modifier
                    .animateItemPlacement()
                    .motionEventSpy {
                        if (it.action == ACTION_DOWN) {
                            touchPositionsX.clear()
                            touchPositionsY.clear()
                        }
                        touchPositionsY.add(it.y)
                        touchPositionsX.add(it.x)
                    },
                background = {
                    SwipeBackground(dismissState = dismissState)
                },
                dismissContent = {
                    WordModelRow(
                        index = index,
                        bgColors = viewModel.state.value.bgColors,
                        onWordClick = { viewModel.getWordData(wordModel.word) },
                        wordModel = wordModel,
                        isLoading = viewModel.state.value.isLoading && viewModel.state.value.currentWordClicked == wordModel.word,
                        isOpen = viewModel.state.value.wordsClicked.contains(wordModel.word)
                    )
                },
                directions = setOf(DismissDirection.StartToEnd)
            )
        }
    }

}


@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(modifier = Modifier, SnackbarHostState())
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun MainScreenNightPreview() {
    MainScreen(modifier = Modifier, SnackbarHostState())
}
