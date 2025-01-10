package com.github.patrickpaul.data.scraper

interface ScraperDataSource {

    suspend fun allScrapers(): List<Scraper>
    suspend fun updateScraper(id: Int, scraper: Scraper)
}