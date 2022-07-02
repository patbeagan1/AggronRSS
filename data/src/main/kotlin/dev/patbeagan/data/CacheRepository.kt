package dev.patbeagan.data

import java.util.logging.Level
import java.util.logging.Logger

class CacheRepository<T>(
    private val remote: Remote<T>,
    private val local: Local<T>,
) {
    suspend fun fetch(url: String, checkExpiry: () -> Boolean = { true }): T = if (checkExpiry()) {
        Logger.getGlobal().log(Level.INFO, "Remote $url")
        remote.fetch(url).also { local.store(it) }
    } else {
        Logger.getGlobal().log(Level.INFO, "Local $url")
        local.fetch(url)
    }

    interface Remote<T> {
        suspend fun fetch(url: String): T
    }

    interface Local<T> {
        suspend fun store(content: T)
        suspend fun fetch(url: String): T
    }
}