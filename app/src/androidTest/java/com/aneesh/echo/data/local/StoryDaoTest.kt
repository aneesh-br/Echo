package com.aneesh.echo.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StoryDaoTest {
    private lateinit var db: EchoDatabase
    private lateinit var storyDao: StoryDao
    private lateinit var bookmarkDao: BookmarkDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, EchoDatabase::class.java).build()
        storyDao = db.storyDao()
        bookmarkDao = db.bookmarkDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun refreshingABookmarkedStory_keepsTheBookmark() = runTest {
        //Arrange
        val entity = listOf(StoryEntity(11, "jelly bean", "A",55, 3))
        storyDao.insertAll(entity)
        bookmarkDao.addBookmark(BookmarkEntity(11, 10047))

        // Act: simulate refresh by inserting new w=entity with same id
        val updatedEntity = listOf(StoryEntity(11, "Jolly Launcher", "B", 65, 33))
        storyDao.insertAll(updatedEntity)

        // Assert:  searchStoriesWithBookmarks("").first() →
        //          single item, new title, bookmark NOT null
        val resultEntity = storyDao.searchStoriesWithBookmarks("").first()
        assertEquals(1, resultEntity.size)
        assertEquals(updatedEntity.first().title, resultEntity.first().story.title)
        assertNotNull(resultEntity.first().bookmark)
    }

    @Test
    fun storyWithoutBookmark_hasNullBookmark() = runTest {
        // Two stories, bookmarked only ONE of them.
        // Assert the bookmarked one has a bookmark and the other has null.
        // This proves @Relation matches the right rows (id ↔ storyId).
        // Tip: sort/find results by story.id rather than trusting list order.

        //Arrange
        val entities = listOf(
            StoryEntity(11, "jelly bean", "A",55, 3),
            StoryEntity(33, "Hey", "B", 33, 5)
        )

        storyDao.insertAll(entities)

        //Act
        bookmarkDao.addBookmark(BookmarkEntity(11, 70947))

        //Assert
        val resultEntity = storyDao.searchStoriesWithBookmarks("").first()
        val story11 = resultEntity.single { it.story.id == 11L }
        val story33 = resultEntity.single { it.story.id == 33L }
        assertNotNull(story11.bookmark)
        assertNull(story33.bookmark)
    }
}