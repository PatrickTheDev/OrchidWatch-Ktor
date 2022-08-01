package com.github.patrickpaul.dao

import com.github.patrickpaul.models.Product
import com.github.patrickpaul.models.Store

interface DAOFacade {

    suspend fun allProducts(): List<Product>
    suspend fun product(id: Int): Product?
    suspend fun addNewProduct(name: String, url: String, price: String, store: Store): Product?
    suspend fun editProduct(id: Int, name: String, url: String, price: String, store: Store): Boolean
    suspend fun deleteProduct(id: Int): Boolean

}