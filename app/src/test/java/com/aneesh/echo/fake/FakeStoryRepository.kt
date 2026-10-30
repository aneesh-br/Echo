package com.aneesh.echo.fake

import com.aneesh.echo.data.StoryRepository
import com.aneesh.echo.data.local.StoryEntity
import com.aneesh.echo.data.local.StoryWithBookmark
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.io.IOException

class FakeStoryRepository : StoryRepository {

    val stories = MutableStateFlow<List<StoryWithBookmark>>(emptyList())

    var failRefresh: Boolean = false

    val queries = mutableListOf<String>()

    override suspend fun refresh() {
        if(failRefresh) {
            throw IOException("Failed to reach server")
        }
    }

    override fun searchStoriesWithBookmarks(query: String): Flow<List<StoryWithBookmark>> {
        queries.add(query)
        return stories
    }

    override fun fetchStory(id: Long): Flow<StoryEntity?> {
        return stories.map { list -> list.find { it.story.id == id }?.story }
    }

}