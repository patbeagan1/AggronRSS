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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import dev.patbeagan.data.RssRepository
import dev.patbeagan.data.config.DatabaseConfig
import dev.patbeagan.data.config.NetworkConfig
import dev.patbeagan.data.dao.Feed
import dev.patbeagan.data.dao.FeedItem
import dev.patbeagan.data.remote.RemoteRssDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

@ExperimentalUnitApi
@Composable
@Preview
fun App() {
    val repository = remember { RssRepository(RemoteRssDataSource(NetworkConfig.httpClient)) }
    val scope = rememberCoroutineScope()

    DatabaseConfig.init()

    scope.launch {
        val fetchBasic = repository.fetchBasic()
        println(fetchBasic)

        newSuspendedTransaction(Dispatchers.IO) {
            SchemaUtils.create(Feed.FeedTable, FeedItem.FeedItemTable)
            Feed.new { title = "Austin" }
            // print sql to std-out
            val newFeed = Feed.new {
                title = "St. Petersburg"
            }
            FeedItem.new {
                title = "title"
                description = "desc"
                feed = newFeed.id
            }

            Feed.new { title = "Boston" }
            // 'select *' SQL: SELECT Cities.id, Cities.title FROM Cities
            println("Cities: ${Feed.all().map { it.title + ",," + it.feedItem.joinToString(",") { it.description } }}")
            println("Cities: ${FeedItem.all().map { it.title }}")
        }
    }
    val scaffoldState = rememberScaffoldState()
    var selectedContent by remember { mutableStateOf(Content(-1, "default")) }
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
                    DrawerPane()
                    SelectionPane(selectedContent) { selectedContent = it }
                    ContentPane(scaffoldState.snackbarHostState, selectedContent)
                }
            }
        }
    }
}

