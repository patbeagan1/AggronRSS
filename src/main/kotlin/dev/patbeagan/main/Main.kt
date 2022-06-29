package  dev.patbeagan.main

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sun.syndication.feed.synd.SyndEntry
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
import org.jetbrains.exposed.sql.transactions.transaction


@ExperimentalUnitApi
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
    ) {
        val repository = remember {
            RssRepository(
                RemoteRssDataSource(NetworkConfig.httpClient),
                url = "https://patbeagan.dev/feed-old.xml"
            )
        }

        DatabaseConfig.init()
        val scope = rememberCoroutineScope()
        scope.launch(Dispatchers.IO) {
            val fetchBasic = repository.fetchBasic()
            println("fetchBasic")
        }
        scope.launch(Dispatchers.IO) {
            createTestData()
            pullFromRemote(repository)
        }

        App()
    }
}

private fun createTestData() {
    transaction {
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

private suspend fun pullFromRemote(repository: RssRepository) {
    repository.fetchRss().let { syndFeed ->
        transaction {
            val newFeed = Feed.new {
                this.title = syndFeed.title
                this.description = syndFeed.description
            }
            syndFeed.entries?.forEach {
                val entry = it as SyndEntry
                FeedItem.new {
                    this.title = entry.title
                    this.description = entry.description.value
                    this.feed = newFeed.id
                }
            }
        }
    }
}
