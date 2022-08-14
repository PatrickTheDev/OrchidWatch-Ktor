package com.github.patrickpaul.data.user

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class User(
    val id: Int = 0,
    val username: String,
    val password: String,
    val salt: String
)

object Users : Table("users") {

    val id = integer("id").autoIncrement()
    val username = varchar("username", 256)
    val password = varchar("password", 256)
    val salt = varchar("salt", 256)

    override val primaryKey = PrimaryKey(id)
}
