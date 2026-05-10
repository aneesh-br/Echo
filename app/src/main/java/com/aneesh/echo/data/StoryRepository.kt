package com.aneesh.echo.data

import com.aneesh.echo.model.Story
import com.aneesh.echo.network.HnApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton //Without it Hilt creates a new StoryRepository instance every time something asks for one — wasteful when it's stateless.
class StoryRepository @Inject constructor(
    private val apiService: HnApiService
){
    suspend fun fetchTopStories() : List<Story> = coroutineScope {
        apiService.getTopStoryIds().take(30).map { id ->
            async {
                apiService.getItem(id)
            }
        }.awaitAll() // async returns Defferred type, .await() makes it return the list
    }
}