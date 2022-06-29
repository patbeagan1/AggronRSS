package dev.patbeagan.data.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class FeedItem(id: EntityID<Int>) : IntEntity(id) {
    var title by FeedItemTable.title
    var description by FeedItemTable.description
    var feed by FeedItemTable.feed

    object FeedItemTable : IntIdTable() {
        val title = varchar("title", 100)
        val description = text("description")
        val feed = reference("feed", Feed.FeedTable)
    }

    companion object : IntEntityClass<FeedItem>(FeedItemTable)
}