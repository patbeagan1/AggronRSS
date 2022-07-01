package dev.patbeagan.data

import dev.patbeagan.data.dao.Feed
import dev.patbeagan.data.dao.FeedItem
import org.jetbrains.exposed.sql.transactions.transaction

class AggronRepository {
    fun getAllFeeds() = transaction {
        Feed.all().toList()
    }

    fun findItemsForFeed(it: Feed): List<FeedItem> = transaction {
        FeedItem.find {
            FeedItem.FeedItemTable.feed eq it.id
        }.toList()
    }
}