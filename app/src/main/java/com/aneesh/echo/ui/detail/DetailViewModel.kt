package com.aneesh.echo.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.aneesh.echo.data.StoryRepository
import com.aneesh.echo.data.local.StoryEntity
import com.aneesh.echo.ui.navigation.DetailScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storyRepository: StoryRepository
): ViewModel() {
    private val storyId = savedStateHandle.toRoute<DetailScreen>().id

    private val _story = MutableStateFlow<StoryEntity?>(null)
    val story: StateFlow<StoryEntity?> = _story

    init {
        viewModelScope.launch {
           storyRepository.fetchStory(storyId).collect {
               _story.value = it
           }
        }
    }
}