package com.aneesh.echo.data

import app.cash.turbine.test
import com.aneesh.echo.data.local.StoryEntity
import com.aneesh.echo.fake.FakeHnApiService
import com.aneesh.echo.fake.FakeStoryDao
import com.aneesh.echo.model.Story
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class StoryRepositoryTest {

    private val fakeStoryDao = FakeStoryDao()
    private val fakeHnApiService = FakeHnApiService()
    private val repository = StoryRepository( fakeHnApiService, fakeStoryDao)

    @Test
    fun refresh_savesFetchedStoriesToDb() = runTest {
        //Arrange
        fakeHnApiService.stories = listOf(
            Story(1, "Kotlin tips",  "aneesh", 100),
            Story(2, "Compose deep", "jorge",  200)
        )

        //Act
        repository.refresh()

        //Assert
        val expected = listOf(
            StoryEntity(1, "Kotlin tips",  "aneesh", 100),
            StoryEntity(2, "Compose deep", "jorge",  200)
        )

        repository.fetchTopStories().test {
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun refresh_oneItemFails_otherStoriesStillSaved() = runTest {
        //Arrange
        fakeHnApiService.stories = listOf(
            Story(1, "Kotlin tips",  "aneesh", 100),
            Story(2, "Broken story", "bob",     50),
            Story(3, "Compose deep", "jorge",  200)
        )
        fakeHnApiService.failingIds = setOf(2L)

        //Act
        repository.refresh()

        //Assert
        val expected = listOf(
            StoryEntity(1, "Kotlin tips",  "aneesh", 100),
            StoryEntity(3, "Compose deep", "jorge",  200)
        )
        repository.fetchTopStories().test {
            assertEquals(expected, awaitItem())
        }
    }
}