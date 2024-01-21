package com.github.patrickpaul.data.product

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
    val inserted: LocalDate,
    val imageUrl: String,
) {

    companion object {
        fun createProduct(
            name: String,
            price: String,
            url: String,
            store: Store,
            inserted: LocalDate,
            imageUrl: String,
        ): Product {
            return Product(
                name = name,
                price = price,
                url = url,
                store = store,
                inserted = inserted,
                imageUrl = imageUrl,
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
    val imageUrl = varchar("imageUrl", 512)

    override val primaryKey = PrimaryKey(id)

}