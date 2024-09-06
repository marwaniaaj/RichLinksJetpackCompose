package com.devtutorial.richlinks.model

import android.util.Log
import org.jsoup.Jsoup
import java.net.URL

sealed class LinkViewState {
    data object Loading: LinkViewState()
    data class Success(val metadata: LinkMetadata): LinkViewState()
    data class Failure(val exception: Exception): LinkViewState()
}

data class LinkMetadata(
    val url: String,
    val host: String,
    val title: String? = null,
    val description: String? = null,
    val imageUrl: String? = null
)

fun fetchMetadata(url: String): LinkViewState {
    return try {
        val host = URL(url).host
        val doc = Jsoup.connect(url).get()
        val title = doc.title()
        val description = doc.select("meta[property=og:description]").attr("content")
        val imageUrl = doc.select("meta[property=og:image]").attr("content")
        LinkViewState.Success(LinkMetadata(url, host, title, description, imageUrl))
    } catch (e: Exception) {
        Log.e("MetadataFetch", "Error fetching metadata", e)
        LinkViewState.Failure(e)
    }
}