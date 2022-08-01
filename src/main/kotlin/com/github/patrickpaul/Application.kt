package com.github.patrickpaul

import com.github.patrickpaul.dao.DatabaseFactory
import com.github.patrickpaul.plugins.configureRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

fun main(args: Array<String>) {

    embeddedServer(Netty, commandLineEnvironment(args)).start(wait = true)

}

fun Application.main() {

    install(Koin) {
        SLF4JLogger()
        // modules(exampleModule)
    }

}

fun Application.module() {

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    DatabaseFactory.init()
    configureRouting()
}