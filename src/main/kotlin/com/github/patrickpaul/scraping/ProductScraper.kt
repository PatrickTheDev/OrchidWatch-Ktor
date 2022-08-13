package com.github.patrickpaul.scraping

import com.github.patrickpaul.models.Product
import com.github.patrickpaul.models.Store
import com.microsoft.playwright.Browser
import com.microsoft.playwright.ElementHandle

abstract class ProductScraper(
    val playwrightBrowser: Browser
) {

    abstract fun scrape(): List<Product>

    abstract fun getProductName(product: ElementHandle): String
    abstract fun getProductPrice(product: ElementHandle): String
    abstract fun getProductURL(product: ElementHandle): String
    abstract fun getProductStore(): Store

}