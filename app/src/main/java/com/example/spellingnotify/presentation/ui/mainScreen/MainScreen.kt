package com.example.spellingnotify.presentation.ui.mainScreen

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.DismissValue.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spellingnotify.presentation.ui.mainScreen.components.SwipeBackground
import com.example.spellingnotify.presentation.ui.mainScreen.components.WordModelRow
import com.example.spellingnotify.presentation.utils.observeWithLifecycle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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

    LazyColumn(
        state = mainListState,
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 72.dp)
    ) {
        itemsIndexed(
            items = viewModel.state.value.wordModels,
            key = { _, wordModel -> wordModel.word }
        ) { index, wordModel ->
            val currentWord by rememberUpdatedState(wordModel)
            val dismissState = rememberDismissState(
                confirmValueChange = {
                    when (it) {
                        DismissedToEnd -> {

                            viewModel.onEvent(MainScreenActions.OnSwipeWord(wordModel.word))
                            true
                        }

                        DismissedToStart -> {
                            true
                        }

                        Default -> {
                            true
                        }
                    }
                }
            )
            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier
                    .animateItemPlacement(),
                background = {
                    SwipeBackground(dismissState = dismissState)
                },
                dismissContent = {
                    WordModelRow(
                        index = index,
                        bgColors = viewModel.state.value.bgColors,
                        onWordClick = { viewModel.getWordData(wordModel.word) },
                        wordModel = wordModel
                    )
                },
                directions =
                if (mainListState.isScrollInProgress)
                    emptySet() else setOf(DismissDirection.StartToEnd),
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
