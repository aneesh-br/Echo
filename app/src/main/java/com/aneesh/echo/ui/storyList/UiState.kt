package com.aneesh.echo.ui.storyList

import com.aneesh.echo.data.local.StoryEntity
import kotlinx.collections.immutable.ImmutableList

sealed class UiState {
    data object Loading: UiState()
    data class Success(val stories: ImmutableList<StoryEntity>): UiState()
    data class Error(val e: Throwable): UiState()
}