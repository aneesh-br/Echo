package com.aneesh.echo.ui.storyList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aneesh.echo.data.StoryRepository
import com.aneesh.echo.data.local.StoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class StoryListViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {
//    private val hardCodedStories = listOf(
//        Story(1, "Coroutines are not threads", "gopher", 342),
//        Story(2, "Jetpack Compose Internal", "Jorge", 289),
//        Story(3, "Why we moved KMP", "Aneesh", 501)
//    )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing

    private val _events = MutableSharedFlow<StoryListEvent>()
    val events : SharedFlow<StoryListEvent> = _events.asSharedFlow()

    val stories: StateFlow<UiState> = _searchQuery
        .debounce(300.milliseconds)
        .distinctUntilChanged()
        .flatMapLatest { query -> // alternate: flatMapLatest(repository::searchStories)
            repository.searchStories(query)
                .map<List<StoryEntity>, UiState>{ // this is Flow.map not List.map
                    UiState.Success(it.toImmutableList())
                }
                .catch { e ->
                    emit(UiState.Error(e)) // emit()
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    fun refresh() {
        android.util.Log.d("EchoRefresh", "refresh() called")
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                repository.refresh()
            }
            catch (e: CancellationException) {
                throw e
            }
            catch (e: Exception) {
                _events.emit(StoryListEvent.ShowSnackBar("Couldn't fetch the latest articles"))
            }
            finally {
                _isRefreshing.value = false
            }
        }
    }

    fun onQueryTextChange(query: String) {
        _searchQuery.value = query
    }
}