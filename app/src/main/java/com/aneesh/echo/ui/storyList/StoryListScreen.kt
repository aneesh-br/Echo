package com.aneesh.echo.ui.storyList

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aneesh.echo.data.local.StoryEntity
import com.aneesh.echo.model.Story

@Composable
fun StoryListRoute(
    modifier: Modifier = Modifier,
    viewModel: StoryListViewModel = hiltViewModel()
) {
    val uiState by viewModel.stories.collectAsStateWithLifecycle()
    StoryListContent(uiState, modifier)
}

@Composable
fun StoryListContent(uiState: UiState, modifier: Modifier = Modifier) {
    when(uiState) {
        is UiState.Success -> {
            val stories = uiState.stories
            LazyColumn(modifier = modifier.fillMaxSize()) {
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