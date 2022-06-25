package dev.patbeagan.data

class RssRepository(
    private val rssRemoteDataSource: RemoteRssDataSource,
) {
    suspend fun fetchBasic() = rssRemoteDataSource.fetchBasic()
}