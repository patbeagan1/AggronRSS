package dev.patbeagan.main

import dev.patbeagan.data.CacheRepository
import dev.patbeagan.data.RssRepository
import dev.patbeagan.data.local.LocalRssDataSource
import dev.patbeagan.data.remote.RemoteRssDataSource
import dev.patbeagan.data.remote.RemoteSampleRssDataSource

class ViewModel() {
    val repository = RssRepository(
        RemoteSampleRssDataSource(),
        url = "https://patbeagan.dev/feed-old.xml"
    )
    val repo = CacheRepository(
        RemoteRssDataSource(),
        LocalRssDataSource()
    )

    fun createTestData() {
        repository.createTestData()
    }
}