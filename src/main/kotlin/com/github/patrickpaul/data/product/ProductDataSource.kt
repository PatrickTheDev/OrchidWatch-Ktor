package com.github.patrickpaul.data.product

import kotlinx.datetime.LocalDate

interface ProductDataSource {

    suspend fun allProducts(): List<Product>
    suspend fun allProductIds(): List<Int>
    suspend fun productById(id: Int): Product?
    suspend fun addNewProduct(name: String, url: String, price: String, store: Store, inserted: LocalDate): Product?
    suspend fun deleteProduct(id: Int): Boolean
    suspend fun deleteAllProducts(): Boolean
    suspend fun deleteAfterDays(days: Int): Int

}