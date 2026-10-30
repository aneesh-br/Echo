package com.aneesh.echo.di

import com.aneesh.echo.data.OfflineFirstStoryRepository
import com.aneesh.echo.data.StoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent :: class)
abstract class RepositoryModule { // Abstract class, not object

    @Binds
    abstract fun bindStoryRepository(impl: OfflineFirstStoryRepository) : StoryRepository
}