package com.github.patrickpaul.scraping

import com.github.patrickpaul.models.Product
import com.github.patrickpaul.models.Store
import com.microsoft.playwright.Browser
import com.microsoft.playwright.ElementHandle
import com.microsoft.playwright.Playwright
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import java.util.*

class SchwerteScraper(private val browser: Browser) : ProductScraper(browser) {

    override fun scrape(): List<Product> {
        val result: MutableList<Product> = LinkedList()

        browser.let {
            val page = it.newPage()
            page.navigate(schwerteShopNewUrl)
            page.waitForTimeout(10000.0) // 10 seconds
            val orchids = page.querySelectorAll(PRODUCT_WRAPPER)
            for (orchid in orchids) {
                result.add(
                    Product.createProduct(
                        getProductName(orchid),
                        getProductPrice(orchid),
                        getProductURL(orchid),
                        getProductStore(),
                        getProductDate()
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
        val manipulatedUrl = product
            .querySelector("a")
            .getAttribute("href")
        val index = manipulatedUrl.indexOf("&")
        return manipulatedUrl.substring(0, index)
    }

    override fun getProductPrice(product: ElementHandle): String {
        val rawPrice = product
            .querySelector(PRODUCT_PRICE)
            .innerText()
        val price = rawPrice.run {
            substring(
                indexOf("Preis") + 6,
                indexOf("EUR") - 1
            )
                .trim()
                .plus(" €")
        }
        return price
    }

    override fun getProductStore(): Store {
        return Store.SCHWERTE
    }

    private companion object {
        const val schwerteStartUrl = "https://shop.schwerter-orchideenzucht.de/"
        const val schwerteShopNewUrl = "https://shop.schwerter-orchideenzucht.de/products_new.php"

        const val PRODUCT_WRAPPER = ".p"
        const val PRODUCT_NAME = "u"
        const val PRODUCT_PRICE = "td:has-text(\"Preis\")"
    }

}