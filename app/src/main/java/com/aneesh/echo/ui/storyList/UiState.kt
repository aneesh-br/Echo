package com.aneesh.echo.ui.storyList

import com.aneesh.echo.data.local.StoryEntity

sealed class UiState {
    data object Loading: UiState()
    data class Success(val stories: List<StoryEntity>): UiState()
    data class Error(val e: Exception): UiState()
}