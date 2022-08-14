package com.github.patrickpaul.scraping

import com.github.patrickpaul.data.product.Product
import com.github.patrickpaul.data.product.Store
import com.microsoft.playwright.Browser
import com.microsoft.playwright.ElementHandle
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

abstract class ProductScraper(
    val playwrightBrowser: Browser
) {

    abstract fun scrape(): List<Product>

    abstract fun getProductName(product: ElementHandle): String
    abstract fun getProductPrice(product: ElementHandle): String
    abstract fun getProductURL(product: ElementHandle): String
    abstract fun getProductStore(): Store

    fun getProductDate() = Clock.System.todayIn(TimeZone.currentSystemDefault())

}