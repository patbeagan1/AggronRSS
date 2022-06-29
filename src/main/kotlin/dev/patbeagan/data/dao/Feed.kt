package dev.patbeagan.data.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class Feed(id: EntityID<Int>) : IntEntity(id) {
    var title: String by FeedTable.title
    var description: String? by FeedTable.description
    val feedItem: List<FeedItem>
        get() = FeedItem.find {
            FeedItem.FeedItemTable.feed eq id
        }.toList()

    object FeedTable : IntIdTable() {
        val title = varchar("title", 100)
        val description = varchar("description", 100)
            .nullable()
            .default(null)
    }

    companion object : IntEntityClass<Feed>(FeedTable)
}

