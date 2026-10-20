package com.aneesh.echo.ui.storyList

import com.aneesh.echo.data.local.StoryWithBookmark
import kotlinx.collections.immutable.ImmutableList

sealed class UiState {
    data object Loading: UiState()
    data class Success(val stories: ImmutableList<StoryWithBookmark>): UiState()
    data class Error(val e: Throwable): UiState()
}