package com.aneesh.echo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Story(
    val id : Long,
    val title: String,
    @SerialName("by")
    val author: String,
    val score: Int
)