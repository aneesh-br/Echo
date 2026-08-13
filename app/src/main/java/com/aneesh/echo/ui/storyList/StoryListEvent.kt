package com.aneesh.echo.ui.storyList

sealed interface StoryListEvent {
    data class ShowSnackBar(val message: String): StoryListEvent
}