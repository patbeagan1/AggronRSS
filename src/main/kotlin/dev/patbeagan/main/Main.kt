package  dev.patbeagan.main

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sun.syndication.feed.synd.SyndContentImpl
import com.sun.syndication.feed.synd.SyndEntry
import com.sun.syndication.feed.synd.SyndEntryImpl
import com.sun.syndication.feed.synd.SyndFeed
import com.sun.syndication.feed.synd.SyndFeedImpl
import com.sun.syndication.io.SyndFeedInput
import com.sun.syndication.io.XmlReader
import dev.patbeagan.data.RssRepository
import dev.patbeagan.data.config.DatabaseConfig
import dev.patbeagan.data.config.NetworkConfig
import dev.patbeagan.data.dao.Feed
import dev.patbeagan.data.dao.FeedItem
import dev.patbeagan.data.remote.RemoteRssDataSource
import dev.patbeagan.ui.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URL
import java.util.logging.Level
import java.util.logging.Logger

val LocalViewModel = compositionLocalOf {
    ViewModel()
}

class RemoteRssDataSource : CacheRepository.Remote<SyndFeed> {
    override suspend fun fetch(url: String): SyndFeed =
        withContext(Dispatchers.IO) {
            SyndFeedInput().build(XmlReader(URL(url)))
        }
}

class LocalRssDataSource(
    val convertFeedToSyndFeedUseCase: ConvertFeedToSyndFeedUseCase = ConvertFeedToSyndFeedUseCase(),
) : CacheRepository.Local<SyndFeed> {
    override suspend fun store(content: SyndFeed) {
        withContext(Dispatchers.IO) {
            transaction {
                val newFeed = Feed.new {
                    this.title = content.title
                    this.description = content.description
                }
                content.entries?.forEach {
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

    override suspend fun fetch(url: String): SyndFeed = withContext(Dispatchers.IO) {
        transaction {
            Feed.find {
                Feed.FeedTable.rssSource.eq(url)
            }.first()
        }
    }.let { convertFeedToSyndFeedUseCase(it) }
}

class ConvertFeedToSyndFeedUseCase(
    val convertFeedItemToSyndEntryUseCase: ConvertFeedItemToSyndEntryUseCase = ConvertFeedItemToSyndEntryUseCase(),
) {
    operator fun invoke(feed: Feed): SyndFeed = SyndFeedImpl().apply {
        this.title = feed.title
        this.link = feed.source
        this.description = feed.description
        this.entries = transaction {
            feed.feedItem
        }.map {
            convertFeedItemToSyndEntryUseCase(it)
        }
    }
}

class ConvertFeedItemToSyndEntryUseCase {
    operator fun invoke(feedItem: FeedItem): SyndEntry = SyndEntryImpl().apply {
        this.title = feedItem.title
        this.description = SyndContentImpl().apply {
            this.value = feedItem.description
        }
    }
}

class ViewModel() {
    private val repository = RssRepository(
        RemoteRssDataSource(NetworkConfig.httpClient),
        url = "https://patbeagan.dev/feed-old.xml"
    )
    val repo = CacheRepository(
        RemoteRssDataSource(),
        LocalRssDataSource()
    )

    private fun createTestData() {
        transaction {
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
}

class CacheRepository<T>(
    private val remote: Remote<T>,
    private val local: Local<T>,
) {
    suspend fun fetch(url: String, checkExpiry: () -> Boolean = { false }): T {
        if (checkExpiry()) {
            Logger.getGlobal().log(Level.INFO, "Remote $url")
            return remote.fetch(url).also { local.store(it) }
        } else {
            Logger.getGlobal().log(Level.INFO, "Local $url")
            return local.fetch(url)
        }
    }

    interface Remote<T> {
        suspend fun fetch(url: String): T
    }

    interface Local<T> {
        suspend fun store(content: T)
        suspend fun fetch(url: String): T
    }
}

@ExperimentalUnitApi
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
    ) {
        DatabaseConfig.init()
        CompositionLocalProvider(
            LocalViewModel provides ViewModel()
        ) {
            App()
        }
    }
}
