package dev.patbeagan.data.transforms

import com.sun.syndication.feed.synd.SyndFeed
import com.sun.syndication.feed.synd.SyndFeedImpl
import dev.patbeagan.data.dao.Feed
import org.jetbrains.exposed.sql.transactions.transaction

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