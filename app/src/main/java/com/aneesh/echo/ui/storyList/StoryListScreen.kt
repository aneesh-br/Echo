package com.aneesh.echo.ui.storyList

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aneesh.echo.data.local.StoryEntity

@Composable
fun StoryListRoute(
    modifier: Modifier = Modifier,
    viewModel: StoryListViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    val lifeCycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }

        lifeCycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
    val uiState by viewModel.stories.collectAsStateWithLifecycle()
    StoryListContent(uiState, modifier)
}

@Composable
fun StoryListContent(uiState: UiState, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {listState.firstVisibleItemIndex}
            .collect { index ->
                Log.d("EchoScroll", "TopItemIndex = $index")
            }
    }
    when(uiState) {
        is UiState.Success -> {
            val stories = uiState.stories
            LazyColumn(state = listState, modifier = modifier.fillMaxSize()) {
                items(stories) { story ->
                    Text(text = story.title)
                    Text(text = story.author)
                }
            }
        }
        is UiState.Loading -> Text("Loading...")
        is UiState.Error -> Text("error: ${uiState.e.message}")
    }
}

@Preview(showBackground = true)
@Composable
fun StoryListContentPreview() {
    val hardCodedStories = listOf(
        StoryEntity(1, "Coroutines are not threads", "gopher", 342),
        StoryEntity(2, "Jetpack Compose Internal", "Jorge", 289),
        StoryEntity(3, "Why we moved KMP", "Aneesh", 501)
    )

    StoryListContent(UiState.Success(hardCodedStories))
}