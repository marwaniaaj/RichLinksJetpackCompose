package com.devtutorial.richlinks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devtutorial.richlinks.ui.LinkItemView
import com.devtutorial.richlinks.ui.theme.RichLinksTheme
import io.ktor.client.*
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    RichLinksTheme {
        MainScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val linksList = listOf(
        "https://not-valid-url", // --> Invalid URL
        "https://m3.material.io/develop/android/jetpack-compose", // --> Valid URL
        "https://expatexplore.com/blog/when-to-travel-weather-seasons/", // --> URL that does not contain image
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = .1f)),
                title = { Text("Rich Links") },
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .wrapContentSize(Alignment.TopCenter),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(linksList) { link ->
                LinkItemView(link = link)
            }
        }
    }
}

expect fun openLink(link: String)

expect fun getHttpClient(): HttpClient

@Composable
expect fun multiplatformAsyncImage(imageUrl: String, modifier: Modifier)