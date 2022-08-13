package com.github.patrickpaul.scraping

import com.github.patrickpaul.models.Product
import com.github.patrickpaul.models.Store
import com.microsoft.playwright.Browser
import com.microsoft.playwright.ElementHandle
import java.util.*
import java.util.stream.Collectors

class CramerScraper(private val browser: Browser) : ProductScraper(browser) {

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
                        getProductStore()
                    )
                )
            }
            page.close()
        }
        return result
    }

    private fun getSoldOrchids(orchids: List<ElementHandle>): List<ElementHandle>? {
        return orchids.stream()
            .filter { orchid: ElementHandle ->
                orchid.querySelectorAll(SOLD_LABEL).size > 0
            }
            .collect(Collectors.toList())
    }

    override fun getProductName(product: ElementHandle): String {
        return product.querySelector(PRODUCT_TITLE).innerText()
    }

    override fun getProductURL(product: ElementHandle): String {
        return cramerShopUrl + product.querySelector(PRODUCT_URL).getAttribute("href")
    }

    override fun getProductPrice(product: ElementHandle): String {
        val price = product.querySelector(PRODUCT_PRICE).innerText()
        return price.replace(",", ".")
    }

    override fun getProductStore(): Store {
        return Store.CRAMER
    }

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