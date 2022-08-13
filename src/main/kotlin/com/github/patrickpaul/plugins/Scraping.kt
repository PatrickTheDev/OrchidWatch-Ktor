package com.github.patrickpaul.plugins

import com.github.patrickpaul.dao.dao
import com.github.patrickpaul.models.Product
import com.github.patrickpaul.scraping.ProductScraper
import com.github.patrickpaul.scraping.SchwerteScraper
import com.github.patrickpaul.util.getKoinInstance
import io.ktor.server.application.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

val productChannel = Channel<Product>()

fun Application.scrape() {
    val schwerteJob = scrapeProductsPeriodically(
        getKoinInstance<SchwerteScraper>()
    )
}

fun Application.persist() {

    CoroutineScope(Dispatchers.IO).launch {
        productChannel.receiveAsFlow().collect {
            dao.addNewProduct(
                it.name,
                it.url,
                it.price,
                it.store
            )
        }
    }

}

fun scrapeProductsPeriodically(
    scraper: ProductScraper,
    interval: Long = 50000L
): Job {
    return CoroutineScope(Dispatchers.IO).launch {
        while (isActive) {
            println("New coroutine run")
            val products = scraper.scrape()
            products.forEach { productChannel.send(it) }
            delay(interval)
        }
    }
}
