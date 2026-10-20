package com.aneesh.echo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories") // needs "" and Capital commands
    fun getAllStoriesFromDb() : Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE title LIKE '%' || :query || '%'") // notice '' around %
    fun searchStories(query: String) : Flow<List<StoryEntity>>

    @Upsert
    suspend fun insertAll(stories: List<StoryEntity>) // @Insert(onConflict = OnConflictStrategy.REPLACE) would delete the story row before reinserting on every refresh, and CASCADE would take the bookmark with it.

    @Query("SELECT * FROM stories WHERE id =:id ")
    fun getStoryDetails(id: Long) : Flow<StoryEntity?>  // Trading off suspend for flow because fetchTopStories() is non suspend
}