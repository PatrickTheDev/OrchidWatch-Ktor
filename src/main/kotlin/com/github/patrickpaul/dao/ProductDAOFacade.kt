package com.github.patrickpaul.dao

import com.github.patrickpaul.models.Product
import com.github.patrickpaul.models.Store
import kotlinx.datetime.LocalDate

interface ProductDAOFacade {

    suspend fun allProducts(): List<Product>
    suspend fun allProductIds(): List<Int>
    suspend fun productById(id: Int): Product?
    suspend fun addNewProduct(name: String, url: String, price: String, store: Store, inserted: LocalDate): Product?
    suspend fun deleteProduct(id: Int): Boolean
    suspend fun deleteAllProducts(): Boolean
    suspend fun deleteAfterDays(days: Int): Int

}