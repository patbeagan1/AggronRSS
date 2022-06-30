package dev.patbeagan.data.config

import dev.patbeagan.data.dao.Feed
import dev.patbeagan.data.dao.FeedItem
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseConfig {
    fun init() = sqlite.apply {
        transaction {
            SchemaUtils.create(
                Feed.FeedTable,
                FeedItem.FeedItemTable
            )
        }
    }

    private val h2 by lazy {
        Database.connect(
            url = "jdbc:h2:mem:test",
            driver = "org.h2.Driver"
        )
    }
    private val sqlite by lazy {
        val home = System.getProperty("user.home")
        Database.connect(
            "jdbc:sqlite:$home/aggron",
            "org.sqlite.JDBC"
        )
    }
}
