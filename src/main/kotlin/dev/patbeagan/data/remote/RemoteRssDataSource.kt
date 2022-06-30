package dev.patbeagan.data.remote

import com.sun.syndication.feed.synd.SyndFeed
import com.sun.syndication.io.SyndFeedInput
import com.sun.syndication.io.XmlReader
import dev.patbeagan.data.CacheRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class RemoteRssDataSource : CacheRepository.Remote<SyndFeed> {
    override suspend fun fetch(url: String): SyndFeed =
        withContext(Dispatchers.IO) {
            SyndFeedInput().build(XmlReader(URL(url)))
        }
}