package com.aneesh.echo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StoryEntity::class], version = 1)
abstract class EchoDatabase: RoomDatabase() { // abstract class and extends RoomDatabase
    abstract fun storyDao() : StoryDao // abstract fun
}