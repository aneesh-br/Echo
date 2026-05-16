package com.aneesh.echo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val author: String,
    val score: Int
)