package dev.patbeagan.data.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class Feed(id: EntityID<Int>) : IntEntity(id) {
    var title: String by FeedTable.title
    val feedItem: List<FeedItem>
        get() = FeedItem.find {
            FeedItem.FeedItemTable.feed eq id
        }.toList()

    object FeedTable : IntIdTable() {
        val title = varchar("title", 50)
    }

    companion object : IntEntityClass<Feed>(FeedTable)
}

class FeedItem(id: EntityID<Int>) : IntEntity(id) {
    var title by FeedItemTable.title
    var description by FeedItemTable.description
    var feed by FeedItemTable.feed

    object FeedItemTable : IntIdTable() {
        val title = varchar("title", 50)
        val description = text("description")
        val feed = reference("feed", Feed.FeedTable)
    }

    companion object : IntEntityClass<FeedItem>(FeedItemTable)
}
