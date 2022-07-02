package dev.patbeagan.data.remote

import com.sun.syndication.io.SyndFeedInput
import com.sun.syndication.io.XmlReader
import dev.patbeagan.data.config.NetworkConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class RemoteSampleRssDataSource(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val httpClient: HttpClient = NetworkConfig.httpClient

    suspend fun fetchBasic(
        url: String,
    ) = withContext(dispatcher) {
        httpClient
            .get(url)
            .bodyAsText()
    }

    suspend fun fetchRss(
        url: String,
    ) = withContext(dispatcher) {
        SyndFeedInput().build(XmlReader(URL(url)))
    }
}


