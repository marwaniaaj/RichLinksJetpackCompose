package com.devtutorial.richlinks.ui

import android.content.Intent
import android.net.Uri
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.devtutorial.richlinks.MainScreen
import com.devtutorial.richlinks.R
import com.devtutorial.richlinks.model.LinkMetadata
import com.devtutorial.richlinks.model.LinkViewState
import com.devtutorial.richlinks.model.fetchMetadata
import com.devtutorial.richlinks.ui.theme.RichLinksTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.os.Parcelable

/**
 * A custom [Saver] object for the [LinkViewState] sealed class.
 *
 * This saver is responsible for saving and restoring instances of [LinkViewState]
 * to and from a format that can be handled by [rememberSaveable].
 *
 * **Note:** The [LinkMetadata] class must implement the [Parcelable] interface.
 */
val LinkViewStateSaver = Saver<LinkViewState, Any>(
    save = { linkViewState ->
        when (linkViewState) {
            LinkViewState.Loading -> "Loading"
            is LinkViewState.Success -> linkViewState.metadata
            is LinkViewState.Failure -> linkViewState.exception.message ?: "Error"
        }
    },
    restore = { savedValue ->
        when (savedValue) {
            is LinkMetadata -> LinkViewState.Success(savedValue)
            is String -> LinkViewState.Failure(Exception(savedValue))
            else -> LinkViewState.Loading
        }
    }
)

@Composable
fun LinkItemView(
    link: String,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var loadingState by rememberSaveable(stateSaver = LinkViewStateSaver) {
        mutableStateOf(LinkViewState.Loading)
    }

    LaunchedEffect(link) {
        scope.launch(Dispatchers.IO) {
            loadingState = fetchMetadata(link)
        }
    }

    Row(modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .fillMaxWidth()
            .height(90.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        when (val state = loadingState) {
            is LinkViewState.Loading -> {
                Box(
                    modifier = Modifier.size(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 5.dp
                    )
                }
                Column(
                    modifier = Modifier.padding(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "",
                        modifier = Modifier.fillMaxWidth().shimmer(),
                        minLines = 2
                    )
                    Text(
                        text = "",
                        modifier = Modifier.fillMaxWidth().shimmer()
                    )
                }
            }

            is LinkViewState.Success -> {
                val metadata = state.metadata
                Row(
                    modifier = modifier
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                            context.startActivity(intent)
                        },
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(metadata.imageUrl)
                            .size(90.dp.value.toInt())
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(15.dp)),
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(
                                modifier = Modifier.size(90.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 5.dp
                                )
                            }
                        },
                        error = {
                            Icon(
                                painter = painterResource(R.drawable.imagesmode),
                                contentDescription = null,
                                modifier = Modifier.size(35.dp),
                                tint = Color.Gray
                            )
                        }
                    )

                    Column(
                        modifier = Modifier.padding(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = metadata.title ?: "Untitled",
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 2
                        )
                        Text(
                            text = metadata.host,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            is LinkViewState.Failure -> {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.link_off),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                        tint = Color.Red
                    )
                    Text(
                        text = "Provided link is invalid",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RichLinksPreview() {
    RichLinksTheme(dynamicColor = false) {
        MainScreen()
    }
}