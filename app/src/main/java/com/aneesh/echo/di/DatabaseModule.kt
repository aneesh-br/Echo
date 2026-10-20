package com.aneesh.echo.di

import android.content.Context
import androidx.room.Room
import com.aneesh.echo.data.local.BookmarkDao
import com.aneesh.echo.data.local.EchoDatabase
import com.aneesh.echo.data.local.MIGRATION_1_2
import com.aneesh.echo.data.local.StoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesEchoDatabase(@ApplicationContext context: Context): EchoDatabase { // Hilt can inject application context
        return Room.databaseBuilder(context, EchoDatabase::class.java, "echo_database")
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun providesStoryDao(echoDatabase: EchoDatabase): StoryDao {
        return echoDatabase.storyDao()
    }

    @Provides
    @Singleton
    fun providesBookmarkDao(echoDatabase: EchoDatabase): BookmarkDao {
        return echoDatabase.bookmarkDao()
    }
}