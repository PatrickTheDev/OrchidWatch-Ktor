package com.github.patrickpaul.data.scraper

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Scraper(
    val id: Int = 0,
    val name: String,
    val isHealthy: Boolean,
) {

    companion object {
        fun createScraper(
            name: String,
            isHealthy: Boolean,
        ): Scraper {
            return Scraper(
                name = name,
                isHealthy = isHealthy,
            )
        }
    }
}

object Scrapers : Table("scrapers") {

    val id = integer("id").autoIncrement()
    val name = varchar("name", 256)
    val isHealthy = bool("isHealthy")

    override val primaryKey = PrimaryKey(id)
}
