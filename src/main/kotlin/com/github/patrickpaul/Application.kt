package com.github.patrickpaul

import com.github.patrickpaul.dao.DatabaseFactory
import com.github.patrickpaul.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()

    configureDI()
    configureSerialization()
    configureRouting()
    configureSecurity()

    persist()
    scrape()
}