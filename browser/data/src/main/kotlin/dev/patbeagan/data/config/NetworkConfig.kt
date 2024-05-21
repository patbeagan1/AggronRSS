package dev.patbeagan.data.config

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

object NetworkConfig {
    val httpClient by lazy { HttpClient(CIO) }
}