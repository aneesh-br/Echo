package com.aneesh.echo.fake

import com.aneesh.echo.data.local.StoryDao
import com.aneesh.echo.data.local.StoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeStoryDao : StoryDao{
    private val table = MutableStateFlow<List<StoryEntity>>(emptyList())

    override fun getAllStoriesFromDb(): Flow<List<StoryEntity>> {
        return table
    }

    override fun getStoryDetails(id: Long): Flow<StoryEntity?> {
        return table.map{ storyList ->
            storyList.firstOrNull { storyEntity ->
                storyEntity.id == id
            }
        }
    }

    override fun searchStories(query: String): Flow<List<StoryEntity>> {
        return table.map { storyList ->
            storyList.filter { storyEntity ->
                storyEntity.title.contains(query, ignoreCase = true)
            }
        }
    }

    override suspend fun insertAll(stories: List<StoryEntity>) {
        val incomingIds = stories.map { it.id }.toSet()
        table.emit(table.value.filterNot { currStory ->
            incomingIds.contains(currStory.id)
        } + stories)
    }
}