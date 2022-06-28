package  dev.patbeagan.main

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.patbeagan.data.RssRepository
import dev.patbeagan.data.config.DatabaseConfig
import dev.patbeagan.data.config.NetworkConfig
import dev.patbeagan.data.dao.Feed
import dev.patbeagan.data.dao.FeedItem
import dev.patbeagan.data.remote.RemoteRssDataSource
import dev.patbeagan.ui.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

@ExperimentalUnitApi
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
    ) {
        val repository = remember { RssRepository(RemoteRssDataSource(NetworkConfig.httpClient)) }

        DatabaseConfig.init()
        val scope = rememberCoroutineScope()
        scope.launch {
            val fetchBasic = repository.fetchBasic()
            println("fetchBasic")
        }
        scope.launch {
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
                FeedItem.new {
                    title = "title2"
                    description = "desc2"
                    feed = newFeed.id
                }
                val newFeed2 = Feed.new { title = "Boston" }
                FeedItem.new {
                    title = "title3"
                    description = "desc3"
                    feed = newFeed2.id
                }
            }
        }
        App()
    }
}
