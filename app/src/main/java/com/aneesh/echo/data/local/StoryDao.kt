package com.aneesh.echo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories") // needs "" and Capital commands
    fun getAllStoriesFromDb() : Flow<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stories: List<StoryEntity>)

    @Query("SELECT * FROM stories WHERE id =:id ")
    fun getStoryDetails(id: Long) : Flow<StoryEntity?>  // Trading off suspend for flow because fetchTopStories() is non suspend
}