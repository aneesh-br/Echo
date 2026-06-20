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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.aneesh.echo.data.local.StoryEntity

@Composable
fun StoryDetailsRoute(
    viewModel: DetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
    ) {

    val storyEntity = viewModel.story.collectAsStateWithLifecycle().value
    StoryDetailsContent(storyEntity, modifier)
}

@Composable
fun StoryDetailsContent(
    storyEntity: StoryEntity?,
    modifier: Modifier = Modifier
    ) {
    if(storyEntity == null) {
        Text(text = "Loading...", modifier)
    }
    else {
        Card(modifier) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https://api.dicebear.com/9.x/identicon/png?seed=${storyEntity.author}",
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(10.dp))
                Column(Modifier.padding(10.dp)) {
                    Text(text = storyEntity.title)
                    Text(text = storyEntity.author)
                    Text(text = storyEntity.score.toString())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoryDetailPreview() {
    StoryDetailsContent(StoryEntity(123L, "title", "author name", 12), Modifier)
}