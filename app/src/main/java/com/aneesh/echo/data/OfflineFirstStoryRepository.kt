package com.aneesh.echo.data

import com.aneesh.echo.data.local.StoryDao
import com.aneesh.echo.data.local.StoryEntity
import com.aneesh.echo.data.local.StoryWithBookmark
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
class OfflineFirstStoryRepository @Inject constructor(
    private val apiService: HnApiService,
    private val storyDao: StoryDao
) : StoryRepository {
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
        }.awaitAll().filterNotNull() // async returns Deferred type, .await() makes it return the list
    }

    override fun searchStoriesWithBookmarks(query: String) : Flow<List<StoryWithBookmark>> {
        return storyDao.searchStoriesWithBookmarks(query)
    }

    override suspend fun refresh(){
        storyDao.insertAll(fetchTopStoriesFromNetwork().map { story ->
            mapStoryToStoryEntity(story)
        }
        )
    }

    override fun fetchStory(id: Long) : Flow<StoryEntity?> {
        return storyDao.getStoryDetails(id)
    }

    private fun mapStoryToStoryEntity(story: Story) : StoryEntity {
        return StoryEntity(story.id, story.title, story.author, story.score, story.commentCount)
    }
}