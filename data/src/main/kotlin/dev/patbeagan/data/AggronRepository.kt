package dev.patbeagan.data

import dev.patbeagan.data.dao.Feed
import dev.patbeagan.data.dao.FeedItem
import dev.patbeagan.domain.entity.FeedEntity
import dev.patbeagan.domain.entity.FeedItemEntity
import org.jetbrains.exposed.sql.transactions.transaction

class AggronRepository {
    fun getAllFeeds() = transaction {
        Feed.all().map {
            FeedEntity(
                it.id.value,
                it.title,
                it.source,
                it.description ?: "None",
            )
        }
    }

    fun findItemsForFeed(feedId: Int?): List<FeedItemEntity> = transaction {
        FeedItem.find {
            FeedItem.FeedItemTable.feed eq feedId
        }.map {
            FeedItemEntity(
                it.id.value,
                it.title,
                it.description,
                it.feed.value
            )
        }
    }
}