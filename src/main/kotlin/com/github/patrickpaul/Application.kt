package com.github.patrickpaul

import com.github.patrickpaul.data.DatabaseFactory
import com.github.patrickpaul.plugins.*
import com.github.patrickpaul.security.token.TokenConfig
import com.github.patrickpaul.util.getKoinInstance
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    DatabaseFactory.init()

    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").toString(),
        audience = environment.config.property("jwt.audience").toString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = "7b0&N%b8nrd5ed03nty89smh9-7d8g&*)FG*^&D)FG_^FD^%)&fnn596e92536dff"
        // System.getenv("JWT_SECRET")
    )

    configureDI()
    configureSerialization()
    configureSecurity(tokenConfig)
    configureRouting(
        userDataSource = getKoinInstance(),
        hashingService = getKoinInstance(),
        tokenService = getKoinInstance(),
        tokenConfig = tokenConfig
    )

    persist()
    scrape()
}