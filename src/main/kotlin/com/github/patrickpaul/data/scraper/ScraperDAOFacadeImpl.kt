package com.github.patrickpaul.data.scraper

import com.github.patrickpaul.data.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class ScraperDAOFacadeImpl : ScraperDataSource {

    private fun resultRowToScraper(row: ResultRow) = Scraper(
        id = row[Scrapers.id],
        name = row[Scrapers.name],
        isHealthy = row[Scrapers.isHealthy],
    )

    override suspend fun allScrapers(): List<Scraper> = dbQuery {
        Scrapers
            .selectAll()
            .map(::resultRowToScraper)
    }

    override suspend fun updateScraper(id: Int, scraper: Scraper): Unit = dbQuery {
        Scrapers
            .update(
                where = { Scrapers.id eq id },
                body = {
                    it[name] = scraper.name
                    it[isHealthy] = scraper.isHealthy
                },
            )
    }
}

// TODO: delete and refactor for DI with Koin
val scraperDAO: ScraperDataSource = ScraperDAOFacadeImpl()