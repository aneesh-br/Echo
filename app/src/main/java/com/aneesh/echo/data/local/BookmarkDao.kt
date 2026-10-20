package com.aneesh.echo.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface BookmarkDao {

    @Upsert
    suspend fun addBookmark(bookmarkEntity: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE storyId = :storyId")
    suspend fun removeBookmark(storyId: Long)
}