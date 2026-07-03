package com.aneesh.echo.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.aneesh.echo.data.local.StoryEntity
import com.aneesh.echo.ui.detail.DetailEvent.ShowSnackBar
import com.aneesh.echo.ui.detail.DetailUiState.Error
import com.aneesh.echo.ui.detail.DetailUiState.Loading
import com.aneesh.echo.ui.detail.DetailUiState.NotFound
import com.aneesh.echo.ui.detail.DetailUiState.Success

@Composable
fun StoryDetailsRoute(
    viewModel: DetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
    ) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when(it) {
                is ShowSnackBar -> {
                    snackBarHostState.showSnackbar(it.message)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {SnackbarHost(snackBarHostState)}
    ) { innerPadding ->
        StoryDetailsContent(
            uiState = uiState,
            modifier = Modifier.padding(innerPadding)
        )
    }

}

@Composable
fun StoryDetailsContent(
    uiState: DetailUiState,
    modifier: Modifier = Modifier
    ) {
    when(uiState) {
        is Error -> {
            Column(modifier) {
                Text(text = "Error fetching the story...", modifier)
                Text(text = "details(for nerds): ${uiState.e}")
            }
        }
        is Loading -> {
            Text(text = "Loading...", modifier)
        }
        is NotFound -> {
            Text(text = "Not Found", modifier)
        }
        is Success -> {
            Card(modifier) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = "https://api.dicebear.com/9.x/identicon/png?seed=${
                            uiState.story.author
                        }",
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.padding(10.dp)) {
                        Text(text = uiState.story.title)
                        Text(text = uiState.story.author)
                        Text(text = uiState.story.score.toString())
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoryDetailPreview() {
    StoryDetailsContent(
        Success(
            StoryEntity(123L, "title", "author name", 12)),
            Modifier
        )
}