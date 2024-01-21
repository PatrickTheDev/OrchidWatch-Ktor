package com.github.patrickpaul

import com.github.patrickpaul.data.DatabaseFactory
import com.github.patrickpaul.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

private const val DEFAULT_PORT = 9999
private const val DEFAULT_HOST = "0.0.0.0"

fun main() {
    embeddedServer(
        Netty,
        port = DEFAULT_PORT,
        host = DEFAULT_HOST,
        module = Application::module,
    )
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()

    configureDI()
    configureSerialization()
    configureRouting()
    configureScraping()
}

// configureSecurity(tokenConfig)

// val tokenConfig = TokenConfig(
//     issuer = environment.config.property("jwt.issuer").toString(),
//     audience = environment.config.property("jwt.audience").toString(),
//     expiresIn = 365L * 1000L * 60L * 60L * 24L,
//     secret = "7b0&N%b8nrd5ed03nty89smh9-7d8g&*)FG*^&D)FG_^FD^%)&fnn596e92536dff"
//     // System.getenv("JWT_SECRET")
// )