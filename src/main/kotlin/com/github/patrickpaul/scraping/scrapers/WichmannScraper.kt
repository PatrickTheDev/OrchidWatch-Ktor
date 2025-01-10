package com.github.patrickpaul.scraping.scrapers

import com.github.patrickpaul.data.product.Product
import com.github.patrickpaul.data.product.Store
import com.github.patrickpaul.scraping.ProductScraper
import com.microsoft.playwright.Browser
import com.microsoft.playwright.ElementHandle
import java.util.*

class WichmannScraper(private val browser: Browser) : ProductScraper() {

    override fun scrape(): List<Product> {
        val result: MutableList<Product> = LinkedList()
        var nextPageUrl: String? = wichmannShopNewUrl
        browser.let {
            val page = it.newPage()
            while (nextPageUrl != null) {
                page.navigate(nextPageUrl)
                page.waitForTimeout(10000.0) // 10 seconds
                val orchids = page.querySelectorAll(PRODUCT_WRAPPER)
                for (orchid in orchids) {
                    result.add(
                        Product.createProduct(
                            getProductName(orchid),
                            getProductPrice(orchid),
                            getProductURL(orchid),
                            getProductStore(),
                            getProductDate(),
                            getProductImageUrl(orchid)
                        )
                    )
                }
                val nextPage = page
                    .querySelector(TOOLBAR_TOP)
                    .querySelector(NEXT_PAGE_BUTTON)
                nextPageUrl = if (nextPage != null) {
                    nextPage.getAttribute("href")
                } else {
                    null
                }
            }
            page.close()
        }
        return result
    }

    override fun getProductName(product: ElementHandle): String {
        var name = "error - name"
        try {
            name = product
                .querySelector(PRODUCT_NAME)
                .innerText()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return name
    }

    override fun getProductPrice(product: ElementHandle): String {
        var price = "error - price"
        try {
            price = product
                .querySelector("span:has-text(\"€\")")
                .innerText()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return price
    }

    override fun getProductURL(product: ElementHandle): String {
        var url = "error - url"
        try {
            url = product
                .querySelector("a")
                .getAttribute("href")
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return url
    }

    private fun getProductImageUrl(product: ElementHandle): String {
        var imageUrl = "error - imageUrl"

        try {
            imageUrl = product
                .querySelector("img")
                .getAttribute("src")
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return imageUrl
    }

    override fun getProductStore() = Store.WICHMANN

    private companion object {
        const val wichmannStartUrl = "https://www.orchideen-wichmann.de/"
        const val wichmannShopNewUrl = "https://www.orchideen-wichmann.de/de/neuheiten.html"

        const val TOOLBAR_TOP = ".toolbar-top"
        const val NEXT_PAGE_BUTTON = "a[title=\"Vor\"]"

        const val PRODUCT_WRAPPER = ".item"
        const val PRODUCT_NAME = ".product-name"
    }

}