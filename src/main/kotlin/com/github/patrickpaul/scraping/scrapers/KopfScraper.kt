package com.github.patrickpaul.scraping.scrapers

import com.github.patrickpaul.data.product.Product
import com.github.patrickpaul.data.product.Store
import com.github.patrickpaul.scraping.ProductScraper
import com.microsoft.playwright.Browser
import com.microsoft.playwright.ElementHandle
import java.util.*

class KopfScraper(private val browser: Browser) : ProductScraper(browser) {

    override fun scrape(): List<Product> {
        val result: MutableList<Product> = LinkedList()
        var lastPageUrl = ""

        browser.let {
            val page = it.newPage()
            page.navigate(kopfShopNewUrl)

            while(!page.url().equals(lastPageUrl)) {
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
                lastPageUrl = page.url()
                try {
                    page
                        .querySelector(PAGES)
                        .querySelector(NEXT_PAGE)
                        .click()
                } catch (e: Exception) {
                    e.printStackTrace()
                    break
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
                .trim()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return name
    }

    override fun getProductURL(product: ElementHandle): String {
        var url = "error - url"
        try {
            url = product
                .querySelector(PRODUCT_NAME)
                .getAttribute("href")
        } catch(e: NullPointerException) {
            e.printStackTrace()
        }
        return url
    }

    override fun getProductPrice(product: ElementHandle): String {
        var price = "error - price"
        try {
            price = product
                .querySelector(PRODUCT_PRICE)
                .innerText()
                .trim()
                .replace("*", "")
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return price
    }

    override fun getProductStore() = Store.KOPF

    private companion object {
        const val kopfStartUrl = "https://www.kopf-orchideen.de/"
        const val kopfShopNewUrl = "https://www.kopf-orchideen.de/Neuheiten?order=name-asc&p=1" // page 1

        const val PRODUCT_WRAPPER = ".product-info"
        const val PRODUCT_NAME = ".product-name"
        const val PRODUCT_PRICE = ".product-price"

        const val PAGES = ".pagination"
        const val NEXT_PAGE = ".page-next"
    }

}