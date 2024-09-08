package com.devtutorial.richlinks.model

import android.os.Parcelable
import android.util.Log
import kotlinx.parcelize.Parcelize
import org.jsoup.Jsoup
import java.net.URL

sealed class LinkViewState {
    data object Loading: LinkViewState()
    data class Success(val metadata: LinkMetadata): LinkViewState()
    data class Failure(val exception: Exception): LinkViewState()
}

@Parcelize
data class LinkMetadata(
    val url: String,
    val host: String,
    val title: String? = null,
    val description: String? = null,
    val imageUrl: String? = null
): Parcelable

/**
 * Fetches metadata for a given URL, such as title, description, and image URL.
 *
 * This function uses [Jsoup](https://jsoup.org/) to parse the HTML content of the URL
 * and extract relevant metadata.
 * It attempts to retrieve the title, description (using the `og:description` meta tag),
 * and image URL (using the `og:image` meta tag).
 *
 * @param url The URL to fetch metadata from.
 * @return A [LinkViewState] object representing the result of the metadata fetch operation.
 */
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