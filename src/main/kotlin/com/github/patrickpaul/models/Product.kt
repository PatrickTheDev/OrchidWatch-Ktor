package com.github.patrickpaul.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date

@Serializable
data class Product(
    val id: Int = 0,
    val name: String,
    val url: String,
    val price: String,
    val store: Store,
    val inserted: LocalDate
    ) {

    companion object {
        fun createProduct(
            name: String,
            price: String,
            url: String,
            store: Store,
            inserted: LocalDate
        ): Product {
            return Product(
                name = name,
                price = price,
                url = url,
                store = store,
                inserted = inserted
            )
        }
    }
}

object Products : Table("products") {

    val id = integer("id").autoIncrement()
    val name = varchar("name", 256)
    val url = varchar("url", 512)
    val price = varchar("price", 256)
    val store = enumerationByName("store", 64, Store::class)
    val inserted = date("inserted")

    override val primaryKey = PrimaryKey(id)

}