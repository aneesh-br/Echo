package com.aneesh.echo.ui.storyList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aneesh.echo.data.StoryRepository
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

    private val _stories = MutableStateFlow<UiState>(UiState.Loading)
    val stories: StateFlow<UiState> = _stories

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing

    init {
        viewModelScope.launch {
            repository.fetchTopStories().collect { value ->
                _stories.value = UiState.Success(value)
            }
        }
    }

    fun refresh() {
        android.util.Log.d("EchoRefresh", "refresh() called")
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                repository.refresh()
            }
            catch (e: Exception) {
                _stories.value = UiState.Error(e)
            }
            finally {
                _isRefreshing.value = false
            }
        }
    }
}