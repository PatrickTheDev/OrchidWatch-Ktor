package com.github.patrickpaul.data.product

import com.github.patrickpaul.data.DatabaseFactory.dbQuery
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.*
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAmount
import java.time.temporal.TemporalUnit

class ProductDAOFacadeImpl : ProductDataSource {

    private fun resultRowToProduct(row: ResultRow) = Product(
        id = row[Products.id],
        name = row[Products.name],
        url = row[Products.url],
        price = row[Products.price],
        store = row[Products.store],
        inserted = row[Products.inserted],
        imageUrl = row[Products.imageUrl],
    )

    override suspend fun allProducts(): List<Product> = dbQuery {
        Products
            .selectAll()
            .orderBy(Products.inserted to SortOrder.DESC)
            .map(::resultRowToProduct)
    }

    override suspend fun allProductIds(): List<Int> = dbQuery {
        val ids = mutableListOf<Int>()

        Products
            .slice(Products.id)
            .selectAll()
            .map {
                ids.add(
                    it[Products.id]
                )
            }

        return@dbQuery ids
    }

    override suspend fun productById(id: Int): Product? = dbQuery {
        Products
            .select { Products.id eq id }
            .map(::resultRowToProduct)
            .singleOrNull()
    }

    private suspend fun productByURL(url: String): Product? = dbQuery {
        Products
            .select { Products.url eq url }
            .limit(1)
            .singleOrNull()
            ?.let { resultRowToProduct(it) }
    }

    override suspend fun addNewProduct(
        name: String,
        url: String,
        price: String,
        store: Store,
        inserted: LocalDateTime,
        imageUrl: String,
    ): Product? = dbQuery {
        val product = productByURL(url)
        if (product == null) {
            val insertStatement = Products.insert {
                it[Products.name] = name
                it[Products.url] = url
                it[Products.price] = price
                it[Products.store] = store
                it[Products.inserted] = inserted
                it[Products.imageUrl] = imageUrl
            }
            insertStatement
                .resultedValues
                ?.singleOrNull()
                ?.let(::resultRowToProduct)
        } else {
            null
        }
    }

    override suspend fun deleteProduct(id: Int): Boolean = dbQuery {
        Products
            .deleteWhere { Products.id eq id } > 0
    }

    override suspend fun deleteAllProducts(): Boolean = dbQuery {
        Products
            .deleteAll() > 0
    }

    override suspend fun deleteAfterDays(days: Int): Int = dbQuery {
        val startAt = Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .toJavaLocalDateTime()
            .minus(days.toLong(), ChronoUnit.DAYS)
            .toKotlinLocalDateTime()

        Products
            .deleteWhere { Products.inserted greaterEq startAt }
    }

}


// TODO: delete and refactor for DI with Koin
val productDAO: ProductDataSource = ProductDAOFacadeImpl()