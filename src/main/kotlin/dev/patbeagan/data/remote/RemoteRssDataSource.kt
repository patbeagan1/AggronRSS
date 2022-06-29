package dev.patbeagan.data.remote

import com.sun.syndication.io.SyndFeedInput
import com.sun.syndication.io.XmlReader
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class RemoteRssDataSource(
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
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


