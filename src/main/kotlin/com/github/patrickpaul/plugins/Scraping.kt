package com.github.patrickpaul.plugins

import com.github.patrickpaul.data.product.dao
import com.github.patrickpaul.data.product.Product
import com.github.patrickpaul.scraping.scrapers.CramerScraper
import com.github.patrickpaul.scraping.ProductScraper
import com.github.patrickpaul.scraping.scrapers.SchwerteScraper
import com.github.patrickpaul.util.getKoinInstance
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Duration.Companion.days

val productChannel = Channel<Product>()

fun scrape() {
    val scrapingJob = scrapeProductsPeriodically(
        getKoinInstance<SchwerteScraper>(),
        getKoinInstance<CramerScraper>()
    )
}

fun persist() {
    CoroutineScope(Dispatchers.IO).launch {
        productChannel.receiveAsFlow().collect {
            val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
            dao.addNewProduct(
                it.name,
                it.url,
                it.price,
                it.store,
                today
            )
        }
    }
}

fun cleanUp() {
    val cleanUpJob = CoroutineScope(Dispatchers.IO).launch {
        while (isActive) {
            /*
            TODO: Remove products if older than 30 days

            fun my_function(days: Int) {
                val startAt = DateTime.now()
                    .withTimeAtStartOfDay()
                    .minusDays(days)

                transaction {
                    MyTable.deleteWhere {
                        MyTable.startAt greaterEq startAt
                    }
                }
            }
             */
            delay(1.days.inWholeMilliseconds)
        }
    }
}

fun scrapeProductsPeriodically(
    vararg scrapers: ProductScraper,
    interval: Long = 50000L
): Job {
    return CoroutineScope(Dispatchers.IO).launch {
        while (isActive) {
            scrapers.forEach { scraper -> scraper.scrape().forEach { productChannel.send(it) } }
            delay(interval)
        }
    }
}
