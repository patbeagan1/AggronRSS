package dev.patbeagan.data

import com.sun.syndication.feed.synd.SyndFeed
import dev.patbeagan.data.remote.RemoteRssDataSource

class RssRepository(
    private val rssRemoteDataSource: RemoteRssDataSource,
    private val url: String,
) {
    suspend fun fetchBasic() = rssRemoteDataSource.fetchBasic(url)
    suspend fun fetchRss(): SyndFeed = rssRemoteDataSource.fetchRss(url)
}

