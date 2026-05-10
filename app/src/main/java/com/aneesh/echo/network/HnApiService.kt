package com.aneesh.echo.network

import com.aneesh.echo.model.Story
import retrofit2.http.GET
import retrofit2.http.Path


interface HnApiService {

    @GET("topstories.json")
    suspend fun getTopStoryIds(): List<Long>

    @GET("item/{id}.json")
    suspend fun getItem(@Path("id")id: Long) : Story
}