package dev.patbeagan.data.local

import com.sun.syndication.feed.synd.SyndEntry
import com.sun.syndication.feed.synd.SyndFeed
import dev.patbeagan.data.dao.Feed
import dev.patbeagan.data.dao.FeedItem
import dev.patbeagan.data.CacheRepository
import dev.patbeagan.data.transforms.ConvertFeedToSyndFeedUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction

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