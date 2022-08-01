package com.github.patrickpaul.dao

import com.github.patrickpaul.dao.DatabaseFactory.dbQuery
import com.github.patrickpaul.models.Product
import com.github.patrickpaul.models.Products
import com.github.patrickpaul.models.Store
import org.jetbrains.exposed.sql.*

class DAOFacadeImpl : DAOFacade {

    private fun resultRowToProduct(row: ResultRow) = Product(
        id = row[Products.id],
        name = row[Products.name],
        url = row[Products.url],
        price = row[Products.price],
        store = row[Products.store]
    )

    override suspend fun allProducts(): List<Product> = dbQuery {
        Products
            .selectAll()
            .map(::resultRowToProduct)
    }

    override suspend fun product(id: Int): Product? = dbQuery {
        Products
            .select { Products.id eq id }
            .map(::resultRowToProduct)
            .singleOrNull()
    }

    override suspend fun addNewProduct(
        name: String,
        url: String,
        price: String,
        store: Store
    ): Product? = dbQuery {
        val insertStatement = Products.insert {
            it[Products.name] = name
            it[Products.url] = url
            it[Products.price] = price
            it[Products.store] = store
        }
        insertStatement
            .resultedValues
            ?.singleOrNull()
            ?.let(::resultRowToProduct)
    }

    override suspend fun editProduct(
        id: Int,
        name: String,
        url: String,
        price: String,
        store: Store
    ): Boolean = dbQuery {
        Products
            .update({ Products.id eq id }) {
                it[Products.name] = name
                it[Products.url] = url
                it[Products.price] = price
                it[Products.store] = store
            } > 0
    }

    override suspend fun deleteProduct(id: Int): Boolean = dbQuery {
        Products
            .deleteWhere { Products.id eq id } > 0
    }
}

val dao: DAOFacade = DAOFacadeImpl()