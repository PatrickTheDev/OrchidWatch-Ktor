package com.github.patrickpaul.scraping.scrapers

import com.github.patrickpaul.data.product.Product
import com.github.patrickpaul.data.product.Store
import com.github.patrickpaul.scraping.ProductScraper
import com.microsoft.playwright.Browser
import com.microsoft.playwright.ElementHandle
import java.util.*

class OrchidHouseScraper(private val browser: Browser) : ProductScraper(browser) {

    override fun scrape(): List<Product> {
        val result: MutableList<Product> = LinkedList()
        browser.let {
            val page = it.newPage()
            page.navigate(orchidHouseShopNewUrl)
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
                .querySelector(PRODUCT_PRICE)
                .querySelector("bdi")
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

    override fun getProductStore() = Store.ORCHID_HOUSE

    private companion object {
        const val orchidHouseStartUrl = "https://orchidhouseasia.com/"
        const val orchidHouseShopNewUrl =
            "https://orchidhouseasia.com/shop/?avia_extended_shop_select=yes&product_order=date"

        const val PRODUCT_WRAPPER = ".inner_product"
        const val PRODUCT_NAME = ".woocommerce-loop-product__title"
        const val PRODUCT_PRICE = ".woocommerce-Price-amount"
    }
}