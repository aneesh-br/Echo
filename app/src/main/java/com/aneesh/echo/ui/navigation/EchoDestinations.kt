package com.aneesh.echo.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object ListScreen

@Serializable
data class DetailScreen (val id: Long)