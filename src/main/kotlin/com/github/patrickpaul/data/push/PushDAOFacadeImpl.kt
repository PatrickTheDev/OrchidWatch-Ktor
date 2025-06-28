package com.github.patrickpaul.data.push

import com.github.patrickpaul.data.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class PushDAOFacadeImpl : PushDataSource {

    private fun resultRowToPushToken(row: ResultRow) = PushToken(
        id = row[PushTokens.id],
        value = row[PushTokens.value],
    )

    override suspend fun allTokens(): List<PushToken> = dbQuery {
        PushTokens
            .selectAll()
            .map(::resultRowToPushToken)
    }

    override suspend fun putToken(token: String): Unit = dbQuery {
        PushTokens
            .insert {
                it[value] = token
            }
    }
}

val pushDAO: PushDataSource = PushDAOFacadeImpl()