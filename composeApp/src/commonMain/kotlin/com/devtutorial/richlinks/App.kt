package com.devtutorial.richlinks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.devtutorial.richlinks.ui.LinkItemView
import com.devtutorial.richlinks.ui.theme.RichLinksTheme
import kotlinx.coroutines.delay
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
    val sampleList = listOf(
        "https://not-valid-url", // --> Invalid URL
        "https://m3.material.io/blog/material-3-compose-stable", // --> Valid URL
        "https://expatexplore.com/blog/when-to-travel-weather-seasons/", // --> URL that does not contain image
    )
    val linksList = remember { mutableStateOf(sampleList) }
    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            linksList.value = emptyList()
            delay(2000)
            linksList.value = sampleList
            pullToRefreshState.endRefresh()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = .1f)
                ),
                title = { Text("Rich Links") },
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .wrapContentSize(Alignment.TopCenter),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(linksList.value) { link ->
                    LinkItemView(link = link)
                }
            }

            PullToRefreshContainer(
                modifier = Modifier
                    .then(
                        if (pullToRefreshState.isRefreshing) {
                            Modifier.padding(top = paddingValues.calculateTopPadding())
                        } else {
                            Modifier
                        }
                    )
                    .align(Alignment.TopCenter),
                state = pullToRefreshState,
            )
        }
    }
}

expect fun openLink(link: String)