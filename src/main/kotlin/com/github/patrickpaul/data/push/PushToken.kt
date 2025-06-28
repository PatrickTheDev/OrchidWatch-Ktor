package com.github.patrickpaul.data.push

import com.github.patrickpaul.data.scraper.Scrapers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class PushToken(
    val id: Int = 0,
    val value: String,
)

object PushTokens : Table("pushtokens") {

    val id = integer("id").autoIncrement()
    val value = varchar("value", 256)

    override val primaryKey = PrimaryKey(Scrapers.id)
}