package com.github.patrickpaul.scraping.scrapers

import com.github.patrickpaul.data.product.Product
import com.github.patrickpaul.data.product.Store
import com.github.patrickpaul.scraping.ProductScraper
import com.microsoft.playwright.Browser
import com.microsoft.playwright.ElementHandle
import java.util.*

class WlodarczykScraper(private val browser: Browser) : ProductScraper(browser) {

    override fun scrape(): List<Product> {
        val result: MutableList<Product> = LinkedList<Product>()

        browser.let {
            val page = browser.newPage()
            page.navigate(wlodarczykShopNewUrl)
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
                .querySelector("h5")
                .innerText()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return name
    }

    override fun getProductPrice(product: ElementHandle): String {
        // TODO: format price in default way
        var price = "error - price"
        try {
            price = product
                .querySelector(PRODUCT_PRICE)
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

    override fun getProductStore() = Store.WLODARCZYK

    private companion object {
        const val wlodarczykStartUrl = "https://www.orchideenwlodarczyk.de/shop/catalog/index.php"
        const val wlodarczykShopNewUrl = "https://www.orchideenwlodarczyk.de/shop/catalog/products_new.php"

        const val PRODUCT_WRAPPER = ".item.col-sm-4.grid-group-item"
        const val PRODUCT_PRICE = ".price"
    }
}