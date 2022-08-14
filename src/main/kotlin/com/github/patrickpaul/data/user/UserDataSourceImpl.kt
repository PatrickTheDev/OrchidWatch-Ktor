package com.github.patrickpaul.data.user

import com.github.patrickpaul.dao.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserDataSourceImpl : UserDataSource {

    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        username = row[Users.username],
        password = row[Users.password],
        salt = row[Users.salt]
    )

    override suspend fun getUserByUsername(username: String): User? = dbQuery {
        Users
            .select { Users.username eq username }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun insertUser(user: User): Boolean = dbQuery {
        val insertStatement = Users.insert {
            it[id] = user.id
            it[username] = user.username
            it[password] = user.password
            it[salt] = user.salt
        }
        insertStatement
            .resultedValues
            ?.singleOrNull() != null
    }
}