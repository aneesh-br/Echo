package com.aneesh.echo.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

//In Kotlin, you can declare a val directly in the file, outside any class and it's called a Top-level property in kotlin.
// It’s public by default, and any file can use it with a normal import:
val MIGRATION_1_2 = object : Migration(1, 2) {              // Kotlin object expression
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE stories ADD COLUMN commentCount INTEGER NOT NULL DEFAULT 0")
        db.execSQL("CREATE TABLE IF NOT EXISTS `bookmarks` (`storyId` INTEGER NOT NULL, `savedAt` INTEGER NOT NULL, PRIMARY KEY(`storyId`), FOREIGN KEY(`storyId`) REFERENCES `stories`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
    }
}