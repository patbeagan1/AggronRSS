package dev.patbeagan.domain.entity

data class FeedEntity(
    val id: Int,
    var title: String,
    var source: String,
    var description: String?,
)