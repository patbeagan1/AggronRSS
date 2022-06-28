package dev.patbeagan.data

import dev.patbeagan.data.remote.RemoteRssDataSource

class RssRepository(
    private val rssRemoteDataSource: RemoteRssDataSource,
) {
    suspend fun fetchBasic() = rssRemoteDataSource.fetchBasic()
}