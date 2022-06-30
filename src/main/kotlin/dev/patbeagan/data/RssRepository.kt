package dev.patbeagan.data

import com.sun.syndication.feed.synd.SyndFeed
import dev.patbeagan.data.remote.RemoteSampleRssDataSource

class RssRepository(
    private val rssRemoteDataSource: RemoteSampleRssDataSource,
    private val url: String,
) {
    suspend fun fetchBasic() = rssRemoteDataSource.fetchBasic(url)
    suspend fun fetchRss(): SyndFeed = rssRemoteDataSource.fetchRss(url)
}

