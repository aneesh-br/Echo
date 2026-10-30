package com.aneesh.echo.ui.storyList

import app.cash.turbine.test
import com.aneesh.echo.fake.FakeStoryRepository
import com.aneesh.echo.data.local.StoryEntity
import com.aneesh.echo.data.local.StoryWithBookmark
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StoryListViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private val repository = FakeStoryRepository()

    private lateinit var viewModel: StoryListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = StoryListViewModel(repository)
    }

    @Test
    fun stories_emitsLoadingThenStoriesFromRepository() = runTest {
        //Arrange
        val entities = listOf(
            StoryWithBookmark(StoryEntity(1, "Kotlin tips", "Aneesh", 100, 11), null),
            StoryWithBookmark(StoryEntity(2, "Compose Deep", "jorge", 200, 9), null),
        )

        repository.stories.emit(entities)

        //Act + Assert
        viewModel.stories.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(entities.toImmutableList()), awaitItem())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun search_waits300msOnQueryUpdate() = runTest {
        //Arrange
        val entities = listOf(
            StoryWithBookmark(StoryEntity(1, "Kotlin tips", "Aneesh", 100, 11), null),
            StoryWithBookmark(StoryEntity(2, "Compose Deep", "jorge", 200, 9), null),
        )
        repository.stories.emit(entities)

        viewModel.stories.test {
            awaitItem() //Loading
            awaitItem() //Success

            // Act
            val before = repository.queries.size
            val query = "kotlin"
            viewModel.onQueryTextChange(query)
            advanceTimeBy(299)
            assertEquals(before, repository.queries.size)
            expectNoEvents()

            //Assert
            advanceTimeBy(1)
            runCurrent()
            assertEquals(before + 1, repository.queries.size)
            assertEquals(query, repository.queries[before])

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun refresh_networkFails_sendsSnackbarEvent() = runTest {
        //Arrange
        repository.failRefresh = true

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