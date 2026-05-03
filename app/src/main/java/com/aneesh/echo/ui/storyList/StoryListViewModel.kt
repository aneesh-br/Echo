package com.aneesh.echo.ui.storyList

import androidx.lifecycle.ViewModel
import com.aneesh.echo.model.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class StoryListViewModel @Inject constructor() : ViewModel() {
    private val hardCodedStories = listOf(
        Story(1, "Coroutines are not threads", "gopher", 342),
        Story(2, "Jetpack Compose Internal", "Jorge", 289),
        Story(3, "Why we moved KMP", "Aneesh", 501)
    )

    private var _stories = MutableStateFlow(hardCodedStories)
    val stories: StateFlow<List<Story>> = _stories


}