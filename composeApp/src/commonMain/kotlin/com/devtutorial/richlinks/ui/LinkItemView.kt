package com.devtutorial.richlinks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devtutorial.richlinks.MultiplatformAsyncImage
import com.devtutorial.richlinks.model.LinkMetadata
import com.devtutorial.richlinks.model.LinkViewState
import com.devtutorial.richlinks.model.fetchMetadata
import com.devtutorial.richlinks.openLink
import org.jetbrains.compose.resources.painterResource
import richlinks.composeapp.generated.resources.Res
import richlinks.composeapp.generated.resources.link_off

@Composable
fun LinkItemView(
    link: String,
    modifier: Modifier = Modifier
) {
    var loadingState by remember { mutableStateOf<LinkViewState>(LinkViewState.Loading) }

    LaunchedEffect(link) {
        loadingState = fetchMetadata(link)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .fillMaxWidth()
            .height(90.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        when (val state = loadingState) {
            is LinkViewState.Loading -> LoadingView()
            is LinkViewState.Success -> SuccessView(state.metadata, link)
            is LinkViewState.Failure -> FailureView()
        }
    }
}

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 5.dp
        )
    }
}

@Composable
private fun SuccessView(metadata: LinkMetadata, link: String) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .clickable { openLink(link) }
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val url = remember { mutableStateOf(metadata.imageUrl ?: "") }
        LaunchedEffect(metadata.imageUrl) {
            url.value = metadata.imageUrl ?: ""
        }
        MultiplatformAsyncImage(
            imageUrl = url.value,
            modifier = Modifier
                .size(74.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = metadata.title ?: "Untitled",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2
            )
            Text(
                text = metadata.host,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun FailureView() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.link_off),
            contentDescription = null,
            modifier = Modifier.size(25.dp),
            tint = Color.Red
        )
        Text(
            text = "Provided link is invalid",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}