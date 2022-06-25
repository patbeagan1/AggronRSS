package dev.patbeagan.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

object NetworkConfig {
    val httpClient by lazy { HttpClient(CIO) }
}