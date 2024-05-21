package dev.patbeagan.data

import com.sun.syndication.feed.synd.SyndFeed
import dev.patbeagan.data.dao.Feed
import dev.patbeagan.data.dao.FeedItem
import dev.patbeagan.data.remote.RemoteSampleRssDataSource
import org.jetbrains.exposed.sql.transactions.transaction

class RssRepository(
    private val rssRemoteDataSource: RemoteSampleRssDataSource,
    private val url: String,
) {
    suspend fun fetchBasic() = rssRemoteDataSource.fetchBasic(url)
    suspend fun fetchRss(): SyndFeed = rssRemoteDataSource.fetchRss(url)
    fun createTestData() {
        transaction {
            Feed.new { title = "Austin" }
            // print sql to std-out
            val newFeed = Feed.new {
                title = "St. Petersburg"
                source = "demo"
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
            val newFeed2 = Feed.new {
                title = "Boston"
                source = "demo"
            }
            FeedItem.new {
                title = "title3"
                description = "desc3"
                feed = newFeed2.id
            }
        }
    }
}

