package com.github.patrickpaul.scraping.scrapers

import com.github.patrickpaul.data.product.Product
import com.github.patrickpaul.data.product.Store
import com.github.patrickpaul.scraping.ProductScraper
import com.microsoft.playwright.Browser
import com.microsoft.playwright.ElementHandle
import java.util.*

class HennisScraper(private val browser: Browser) : ProductScraper(browser) {

    override fun scrape(): List<Product> {
        val result: MutableList<Product> = LinkedList()

        browser.let {
            val page = it.newPage()
            page.navigate(hennisShopNewUrl)
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
                        ""
                    )
                )
            }
            page.close()
        }

        return result
    }

    override fun getProductName(product: ElementHandle): String {
        return product
            .querySelector(PRODUCT_NAME)
            .innerText()
    }

    override fun getProductURL(product: ElementHandle): String {
        return hennisShopUrl + product
            .querySelector("a")
            .getAttribute("href")
    }

    override fun getProductPrice(product: ElementHandle) = "Scraping the price is not possible."

    override fun getProductStore() = Store.HENNIS

    private companion object {
        const val hennisStartUrl = "https://hennis-orchideen.de/"
        const val hennisShopUrl = "https://hennis-orchideen.de"
        const val hennisShopNewUrl =
        "https://hennis-orchideen.de/collections/alle-produkte?sort_by=created-descending"

        const val PRODUCT_WRAPPER = ".grid-item__content"
        const val PRODUCT_NAME = ".grid-product__title"
        const val PRODUCT_PRICE = "td:has-text(\"Preis\")"
    }

}