package com.github.patrickpaul.scraping.scrapers

import com.github.patrickpaul.data.product.Product
import com.github.patrickpaul.data.product.Store
import com.github.patrickpaul.scraping.ProductScraper
import com.microsoft.playwright.Browser
import com.microsoft.playwright.ElementHandle
import java.util.*

class CramerScraper(private val browser: Browser) : ProductScraper() {

    override fun scrape(): List<Product> {
        val result: MutableList<Product> = LinkedList()

        browser.let {
            val page = it.newPage()
            page.navigate(cramerShopNewUrl)
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
        var name = "error - name"
        try {
            name = product
                .querySelector(PRODUCT_TITLE)
                .innerText()
        } catch(e: NullPointerException) {
            e.printStackTrace()
        }
        return name
    }

    override fun getProductURL(product: ElementHandle): String {
        var url = "error - url"
        try {
            url = cramerShopUrl + product
                .querySelector(PRODUCT_URL)
                .getAttribute("href")
        } catch (e: NullPointerException) {
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
                .replace(",", ".")
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return price
    }

    override fun getProductStore() = Store.CRAMER

    private companion object {
        const val cramerStartUrl = "https://www.cramer-orchideen.de/index.php"
        const val cramerShopUrl = "https://www.cramer-orchideen.de/shop.php"
        const val cramerShopNewUrl = "https://www.cramer-orchideen.de/shop.php#!/Neu-im-Online-Gew%C3%A4chshaus/c/83688433"

        const val PRODUCT_WRAPPER = ".grid-product__wrap-inner"
        const val PRODUCT_TITLE = ".grid-product__title-inner"
        const val PRODUCT_PRICE = ".grid-product__price-value.ec-price-item"
        const val PRODUCT_URL = ".grid-product__title"

        const val SOLD_LABEL = ".ec-label.label--attention"
    }

}