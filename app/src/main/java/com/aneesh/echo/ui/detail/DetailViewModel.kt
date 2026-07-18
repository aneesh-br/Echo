package com.aneesh.echo.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.aneesh.echo.data.StoryRepository
import com.aneesh.echo.ui.navigation.DetailScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storyRepository: StoryRepository
): ViewModel() {
    private val storyId = savedStateHandle.toRoute<DetailScreen>().id

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<DetailEvent>()
    val events: SharedFlow<DetailEvent> = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            try {
                storyRepository.fetchStory(storyId).collect { entity ->
                    if(entity != null) {
                        _uiState.value = DetailUiState.Success(entity)
                    }
                    else {
                        _uiState.value = DetailUiState.NotFound
                        _events.emit(DetailEvent.ShowSnackBar("Story Not Found"))
                    }
                }
            }
            catch (e: CancellationException) {
                throw e
            }
            catch (e: Exception) {
                 _uiState.value = DetailUiState.Error(e)
            }
        }
    }
}