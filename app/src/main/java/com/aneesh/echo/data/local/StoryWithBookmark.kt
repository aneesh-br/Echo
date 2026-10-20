package com.aneesh.echo.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class StoryWithBookmark(
    @Embedded val story: StoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "storyId"
    )
    val bookmark: BookmarkEntity?
)
