package com.aneesh.echo.fake

import com.aneesh.echo.model.Story
import com.aneesh.echo.network.HnApiService
import java.io.IOException

class FakeHnApiService : HnApiService {
    var stories: List<Story> = emptyList()
    var failAll: Boolean = false
    var failingIds: Set<Long> = emptySet()

    override suspend fun getItem(id: Long): Story {
        if(failAll || failingIds.contains(id)) {
            throw IOException("failed to reach server")
        }

        return stories.find { story ->
            story.id == id
        } ?:throw IOException("No such story")
    }

    override suspend fun getTopStoryIds(): List<Long> {
        if(failAll) {
            throw IOException("failed to reach server")
        }

        return stories.map { story ->
            story.id
        }
    }
}