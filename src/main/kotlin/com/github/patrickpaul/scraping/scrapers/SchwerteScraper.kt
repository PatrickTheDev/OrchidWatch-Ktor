package com.github.patrickpaul.scraping.scrapers

import com.github.patrickpaul.data.product.Product
import com.github.patrickpaul.data.product.Store
import com.github.patrickpaul.scraping.ProductScraper
import com.microsoft.playwright.Browser
import com.microsoft.playwright.ElementHandle
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
                        getProductDate(),
                        getProductImageUrl(orchid)
                    )
                )
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

    override fun getProductURL(product: ElementHandle): String {
        var url = "error - url"
        try {
            val manipulatedUrl = product
                .querySelector("a")
                .getAttribute("href")
            val index = manipulatedUrl.indexOf("&")
            url = manipulatedUrl.substring(0, index)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return url
    }

    override fun getProductPrice(product: ElementHandle): String {
        var price = "error - price"
        try {
            val rawPrice = product
                .querySelector(PRODUCT_PRICE)
                .innerText()
            price = rawPrice.run {
                substring(
                    indexOf("Preis") + 6,
                    indexOf("EUR") - 1
                )
                    .trim()
                    .plus(" €")
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return price
    }

    fun getProductImageUrl(product: ElementHandle): String {
        var imageUrl = "error - imageUrl"
        try {
            val rawImageUrl = product
                .querySelector("td")
                .querySelector("img")
                .getAttribute("src")
            imageUrl = schwerteStartUrl + rawImageUrl
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return imageUrl
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