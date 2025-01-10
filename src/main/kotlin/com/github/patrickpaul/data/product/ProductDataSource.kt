package com.github.patrickpaul.data.product

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface ProductDataSource {

    suspend fun allProducts(): List<Product>
    suspend fun allProductIds(): List<Int>
    suspend fun productById(id: Int): Product?
    suspend fun addNewProduct(name: String, url: String, price: String, store: Store,
                              inserted: LocalDateTime, imageUrl: String): Product?
    suspend fun deleteProduct(id: Int): Boolean
    suspend fun deleteAllProducts(): Boolean
    suspend fun deleteAfterDays(days: Int): Int
}