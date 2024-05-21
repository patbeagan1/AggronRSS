package dev.patbeagan.domain.entity

data class FeedItemEntity(
    val id: Int,
    val title: String,
    val description: String,
    val feed: Int,
)