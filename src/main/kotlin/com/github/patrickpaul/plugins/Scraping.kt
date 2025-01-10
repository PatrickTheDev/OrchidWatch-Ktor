package com.github.patrickpaul.plugins

import com.github.patrickpaul.data.product.productDAO
import com.github.patrickpaul.data.product.Product
import com.github.patrickpaul.scraping.ProductScraper
import com.github.patrickpaul.scraping.scrapers.*
import com.github.patrickpaul.util.getKoinInstance
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.datetime.*
import kotlin.time.Duration.Companion.days

private const val DEFAULT_SCRAPING_INTERVAL = 20L * 60L * 1000L

val productChannel = Channel<Product>()

fun configureScraping() {
    scrape()
    persist()
    cleanUpEveryDay()
}

private fun scrape() {
    val scrapingJob = scrapeProductsPeriodically(
        getKoinInstance<CramerScraper>(),
        getKoinInstance<HennisScraper>(),
        // getKoinInstance<KopfScraper>(),
        // getKoinInstance<OrchidHouseScraper>(),
        getKoinInstance<SchwerteScraper>(),
        getKoinInstance<WichmannScraper>(),
        getKoinInstance<WlodarczykScraper>()
    )
}

private fun persist() {
    CoroutineScope(Dispatchers.IO).launch {
        productChannel.receiveAsFlow().collect {
            val today: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            productDAO.addNewProduct(
                it.name,
                it.url,
                it.price,
                it.store,
                today,
                it.imageUrl
            )
        }
    }
}

private fun cleanUpEveryDay() {
    val cleanUpJob = CoroutineScope(Dispatchers.IO).launch {
        while (isActive) {
            productDAO.deleteAfterDays(30)
            delay(1.days.inWholeMilliseconds)
        }
    }
}

private fun scrapeProductsPeriodically(
    vararg scrapers: ProductScraper,
    interval: Long = DEFAULT_SCRAPING_INTERVAL,
): Job {
    return CoroutineScope(Dispatchers.IO).launch {
        while (isActive) {
            scrapers.forEach { scraper ->
                scraper.scrape().forEach { productChannel.send(it) }
            }
            delay(interval)
        }
    }
}
