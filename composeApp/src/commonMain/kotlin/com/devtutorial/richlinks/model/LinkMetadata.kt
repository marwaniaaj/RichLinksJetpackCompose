package com.devtutorial.richlinks.model

import com.devtutorial.richlinks.network.HttpClient
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

sealed class LinkViewState {
    data object Loading : LinkViewState()
    data class Success(val metadata: LinkMetadata) : LinkViewState()
    data class Failure(val exception: Exception) : LinkViewState()
}

@Serializable
data class LinkMetadata(
    val url: String,
    val host: String,
    val title: String? = null,
    val description: String? = null,
    val imageUrl: String? = null
)

/**
 * Fetches metadata for a given URL, such as title, description, and image URL.
 *
 * This function uses Ktor for the HTTP request and ksoup to parse the HTML content
 * and extract relevant metadata.
 *
 * @param url The URL to fetch metadata from.
 * @return A [LinkViewState] object representing the result of the metadata fetch operation.
 */
suspend fun fetchMetadata(url: Url): LinkViewState {

    return runCatching {
        val response = HttpClient.client.get(url)

        val htmlContent: String = response.bodyAsText()
        if (htmlContent.isEmpty()) {
            return LinkViewState.Failure(Exception("Empty response from server"))
        }

        // Parse HTML using KSoup
        val doc: Document = try {
            Ksoup.parse(htmlContent, url.fullPath)
        } catch (e: Exception) {
            return LinkViewState.Failure(Exception("Failed to parse HTML: ${e.message}"))
        }

        // Extract metadata
        val title = doc.title()
        val description = doc.select("meta[property=og:description]").attr("content")
        val imageUrl = doc.select("meta[property=og:image]").attr("content")

        LinkViewState.Success(LinkMetadata(url.fullPath, response.request.url.host, title, description, imageUrl))
    }.getOrElse { throwable ->
        println("MetadataFetch Error fetching metadata: ${throwable.message}")
        throwable.printStackTrace()
        return LinkViewState.Failure(Exception(throwable))
    }
}