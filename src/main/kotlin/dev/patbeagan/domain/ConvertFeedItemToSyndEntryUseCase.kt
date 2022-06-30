package dev.patbeagan.domain

import com.sun.syndication.feed.synd.SyndContentImpl
import com.sun.syndication.feed.synd.SyndEntry
import com.sun.syndication.feed.synd.SyndEntryImpl
import dev.patbeagan.data.dao.FeedItem

class ConvertFeedItemToSyndEntryUseCase {
    operator fun invoke(feedItem: FeedItem): SyndEntry = SyndEntryImpl().apply {
        this.title = feedItem.title
        this.description = SyndContentImpl().apply {
            this.value = feedItem.description
        }
    }
}