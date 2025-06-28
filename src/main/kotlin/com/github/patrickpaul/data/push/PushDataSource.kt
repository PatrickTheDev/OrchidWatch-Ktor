package com.github.patrickpaul.data.push

interface PushDataSource {

    suspend fun allTokens(): List<PushToken>
    suspend fun putToken(token: String)
}