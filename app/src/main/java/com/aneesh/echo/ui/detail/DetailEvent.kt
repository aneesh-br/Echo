package com.aneesh.echo.ui.detail

// One-off events: things that should happen EXACTLY once and must NOT re-fire on rotation.
// These travel on a SharedFlow (replay = 0), never inside DetailUiState.
sealed interface DetailEvent {
    data class ShowSnackBar(val message: String): DetailEvent
}
