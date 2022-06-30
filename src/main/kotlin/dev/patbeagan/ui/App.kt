package dev.patbeagan.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import dev.patbeagan.data.dao.Feed
import dev.patbeagan.data.dao.FeedItem
import dev.patbeagan.main.LocalViewModel
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.transaction

@ExperimentalUnitApi
@Composable
@Preview
fun App() {
    var feeds by remember { mutableStateOf(listOf<Feed>()) }
    var feedItems by remember { mutableStateOf(listOf<FeedItem>()) }
    val scaffoldState = rememberScaffoldState()
    var selectedFeed by remember { mutableStateOf<Feed?>(null) }
    var selectedFeedItem by remember { mutableStateOf<FeedItem?>(null) }
    val scope = rememberCoroutineScope()
    val repo = LocalViewModel.current.repo
    scope.launch {
//        repo.fetch("https://patbeagan.dev/feed-old.xml")
        transaction {
            feeds = Feed.all().toList()
            
        }
    }
    LaunchedEffect(selectedFeed) {
        selectedFeed?.let {
            transaction {
                feedItems = FeedItem.find {
                    FeedItem.FeedItemTable.feed eq it.id
                }.toList()
            }
        }
    }
    MaterialTheme {
        Scaffold(
            topBar = { TopNav() },
            snackbarHost = { SnackbarHost(scaffoldState.snackbarHostState) }
        ) {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(Color.Blue)
                ) {
                    DrawerPane(modifier = Modifier.background(Color.Green)) {
                        feeds.forEach { feed ->
                            FeedRowItem(
                                dev.patbeagan.ui.state.FeedItem(
                                    feed.hashCode(),
                                    feed.title,
                                    feed.description ?: "NONE"
                                ),
                                selectedFeed?.id == feed.id
                            ) {
                                selectedFeed = feed
                            }
                        }
                    }
                    DrawerPane {
                        feedItems.forEach { dataFeed ->
                            FeedRowItem(
                                dev.patbeagan.ui.state.FeedItem(
                                    dataFeed.hashCode(),
                                    dataFeed.title,
                                    dataFeed.description.take(100)
                                ),
                                selectedFeedItem?.id == dataFeed.id
                            ) {
                                selectedFeedItem = dataFeed
                            }
                        }
                    }
                    ContentPane(scaffoldState.snackbarHostState, selectedFeedItem)
                }
            }
        }
    }
}

