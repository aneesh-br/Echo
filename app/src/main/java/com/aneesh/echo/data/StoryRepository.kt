package com.aneesh.echo.data

import com.aneesh.echo.data.local.StoryEntity
import com.aneesh.echo.data.local.StoryWithBookmark
import kotlinx.coroutines.flow.Flow

interface StoryRepository {

    suspend fun refresh()

    fun searchStoriesWithBookmarks(query: String) : Flow<List<StoryWithBookmark>>

    fun fetchStory(id: Long) : Flow<StoryEntity?>
}