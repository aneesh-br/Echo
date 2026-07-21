package com.aneesh.echo.data

import com.aneesh.echo.data.local.StoryDao
import com.aneesh.echo.data.local.StoryEntity
import com.aneesh.echo.model.Story
import com.aneesh.echo.network.HnApiService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton //Without it Hilt creates a new StoryRepository instance every time something asks for one — wasteful when it's stateless.
class StoryRepository @Inject constructor(
    private val apiService: HnApiService,
    private val storyDao: StoryDao
){
    private suspend fun fetchTopStoriesFromNetwork() : List<Story> = supervisorScope {
        apiService.getTopStoryIds().take(30).map { id ->
            async {
                try {
                    apiService.getItem(id)
                }
                catch (e: CancellationException) {
                    throw e
                }
                catch (_: Exception) {
                    null
                }
            }
        }.awaitAll().filterNotNull() // async returns Defferred type, .await() makes it return the list
    }

    fun searchStories(query: String) : Flow<List<StoryEntity>> {
        return storyDao.searchStories(query)
    }

    suspend fun refresh(){
        storyDao.insertAll(fetchTopStoriesFromNetwork().map { story ->
            mapStoryToStoryEntity(story)
        }
        )
    }

    fun fetchTopStories() : Flow<List<StoryEntity>> {
        return storyDao.getAllStoriesFromDb()
    }

    fun fetchStory(id: Long) : Flow<StoryEntity?> {
        return storyDao.getStoryDetails(id)
    }

    fun mapStoryToStoryEntity(story: Story) : StoryEntity {
        return StoryEntity(story.id, story.title, story.author, story.score)
    }

    fun mapStoryEntityToStory(storyEntity: StoryEntity) : Story {
        return Story(storyEntity.id, storyEntity.title, storyEntity.author, storyEntity.score)
    }
}