package com.aneesh.echo.ui.storyList

import app.cash.turbine.test
import com.aneesh.echo.data.StoryRepository
import com.aneesh.echo.data.local.StoryEntity
import com.aneesh.echo.data.local.StoryWithBookmark
import com.aneesh.echo.fake.FakeHnApiService
import com.aneesh.echo.fake.FakeStoryDao
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StoryListViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val fakeStoryDao = FakeStoryDao()
    private val fakeHnApiService = FakeHnApiService()
    private val repository = StoryRepository(fakeHnApiService, fakeStoryDao)

    private lateinit var viewModel: StoryListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = StoryListViewModel(repository)
    }

    @Test
    fun stories_emitsLoadingThenStoriesFromDB() = runTest {
        //Arrange
        val entities = listOf(
            StoryEntity(1, "Kotlin tips", "Aneesh", 100, 11),
            StoryEntity(2, "Compose Deep", "jorge", 200, 9),
        )

        fakeStoryDao.insertAll(entities)

        //Act + Assert
        viewModel.stories.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(entities.map { StoryWithBookmark(it, bookmark = null) }.toImmutableList()), awaitItem())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun search_waits300msThenShowsOnlyMatchingStories() = runTest {
        //Arrange
        val entities = listOf(
            StoryEntity(1, "Kotlin tips", "Aneesh", 100, 11),
            StoryEntity(2, "Compose Deep", "jorge", 200, 9),
        )
        fakeStoryDao.insertAll(entities)

        viewModel.stories.test {
            awaitItem() //Loading
            awaitItem() //Success

            // Act
            viewModel.onQueryTextChange("kotlin")
            advanceTimeBy(299)
            expectNoEvents()

            //Assert
            assertEquals(UiState.Success(listOf(StoryWithBookmark(entities[0], null)).toImmutableList()), awaitItem())
        }
    }

    @Test
    fun refresh_networkFails_sendsSnackbarEvent() = runTest {
        //Arrange
        fakeHnApiService.failAll = true

        viewModel.events.test {
            //Act
            viewModel.refresh()

            //Assert
            assertEquals(
                StoryListEvent.ShowSnackBar("Couldn't fetch the latest articles"),
                awaitItem()
            )
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}