package com.aneesh.echo.ui.storyList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aneesh.echo.data.StoryRepository
import com.aneesh.echo.model.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryListViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {
//    private val hardCodedStories = listOf(
//        Story(1, "Coroutines are not threads", "gopher", 342),
//        Story(2, "Jetpack Compose Internal", "Jorge", 289),
//        Story(3, "Why we moved KMP", "Aneesh", 501)
//    )

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories: StateFlow<List<Story>> = _stories

    init {
        viewModelScope.launch {
            _stories.emit(repository.fetchTopStories())
        }
    }
}