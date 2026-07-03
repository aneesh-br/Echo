package com.aneesh.echo.ui.detail

import com.aneesh.echo.data.local.StoryEntity

sealed interface DetailUiState {

    data object Loading: DetailUiState

    data class Success(val story: StoryEntity): DetailUiState

    data object NotFound: DetailUiState

    data class Error(val e: Exception): DetailUiState

}
