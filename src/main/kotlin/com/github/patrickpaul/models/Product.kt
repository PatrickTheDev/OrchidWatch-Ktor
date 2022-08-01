package com.github.patrickpaul.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val url: String,
    val price: String,
    val store: Store
    )

object Products : Table() {

    val id = integer("id").autoIncrement()
    val name = varchar("name", 256)
    val url = varchar("url", 512)
    val price = varchar("price", 32)
    val store = enumerationByName("store", 64, Store::class)

    override val primaryKey = PrimaryKey(id)

}