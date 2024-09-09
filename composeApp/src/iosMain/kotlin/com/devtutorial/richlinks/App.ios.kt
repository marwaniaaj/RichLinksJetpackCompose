package com.devtutorial.richlinks

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.painterResource
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import richlinks.composeapp.generated.resources.Res
import richlinks.composeapp.generated.resources.imagesmode

actual fun openLink(link: String) {
    runCatching {
        val nsUrl = NSURL.URLWithString(link)
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }.getOrElse {
        it.printStackTrace()
    }
}

actual fun getHttpClient(): HttpClient {
    return HttpClient(Darwin) {
        val json = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.NONE
        }
        install(ContentNegotiation) {
            json(json)
        }
    }
}

@Composable
actual fun MultiplatformAsyncImage(imageUrl: String, modifier: Modifier) {
    runCatching {
        val state = remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }
        SubcomposeAsyncImage(
            modifier = modifier,
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            onState = {
                state.value = it
            }
        ) {
            when (state.value) {
                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator()
                }

                is AsyncImagePainter.State.Error -> {
                    Icon(
                        painter = painterResource(Res.drawable.imagesmode),
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )
                }

                else -> {
                    SubcomposeAsyncImageContent(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(15.dp))
                    )
                }
            }
        }
    }.getOrElse {
        Icon(
            painter = painterResource(Res.drawable.imagesmode),
            contentDescription = null,
            modifier = Modifier.size(35.dp)
        )
    }
}