package dev.patbeagan.data.config

import org.jetbrains.exposed.sql.Database

object DatabaseConfig {
    fun init() = sqlite

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
