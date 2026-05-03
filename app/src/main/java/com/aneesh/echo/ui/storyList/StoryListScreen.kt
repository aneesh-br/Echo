package com.aneesh.echo.ui.storyList

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aneesh.echo.model.Story


@Composable
fun StoryListScreen(
    modifier: Modifier = Modifier,
    viewModel: StoryListViewModel = hiltViewModel()
) {
    val stories by viewModel.stories.collectAsStateWithLifecycle()
    StoryListContent(stories = stories, modifier = modifier)
}
@Composable
fun StoryListContent(stories: List<Story>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(stories) { story ->
            Text(text = story.title)
            Text(text = story.author)
        }
    }
}