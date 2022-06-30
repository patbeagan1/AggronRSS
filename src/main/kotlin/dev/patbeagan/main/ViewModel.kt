package dev.patbeagan.main

import dev.patbeagan.data.CacheRepository
import dev.patbeagan.data.RssRepository
import dev.patbeagan.data.config.NetworkConfig
import dev.patbeagan.data.dao.Feed
import dev.patbeagan.data.dao.FeedItem
import dev.patbeagan.data.local.LocalRssDataSource
import dev.patbeagan.data.remote.RemoteRssDataSource
import dev.patbeagan.data.remote.RemoteSampleRssDataSource
import org.jetbrains.exposed.sql.transactions.transaction

class ViewModel() {
    val repository = RssRepository(
        RemoteSampleRssDataSource(NetworkConfig.httpClient),
        url = "https://patbeagan.dev/feed-old.xml"
    )
    val repo = CacheRepository(
        RemoteRssDataSource(),
        LocalRssDataSource()
    )

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