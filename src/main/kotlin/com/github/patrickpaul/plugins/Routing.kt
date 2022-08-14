package com.github.patrickpaul.plugins

import com.github.patrickpaul.data.user.UserDataSource
import com.github.patrickpaul.plugins.routes.scraping
import com.github.patrickpaul.security.hashing.HashingService
import com.github.patrickpaul.security.token.TokenConfig
import com.github.patrickpaul.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        signIn(userDataSource, hashingService, tokenService, tokenConfig)
        signUp(hashingService, userDataSource)
        authenticate()
        getSecretInfo()
        scraping()
    }
}