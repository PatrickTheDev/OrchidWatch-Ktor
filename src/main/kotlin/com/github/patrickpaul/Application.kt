package com.github.patrickpaul

import com.github.patrickpaul.dao.DatabaseFactory
import com.github.patrickpaul.plugins.configureRouting
import com.github.patrickpaul.plugins.persist
import com.github.patrickpaul.plugins.scrape
import com.github.patrickpaul.scraping.CramerScraper
import com.github.patrickpaul.scraping.SchwerteScraper
import com.microsoft.playwright.Playwright
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

fun main(args: Array<String>) {

    embeddedServer(Netty, port = 8080) {
        module()
    }.start(wait = true)

}

fun Application.module() {

    install(Koin) {
        SLF4JLogger()
        modules(mainModule)
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    DatabaseFactory.init()
    configureRouting()

    persist()
    scrape()

}

val mainModule = module {

    single { Playwright.create().firefox().launch() } withOptions { createdAtStart() }

    single { CramerScraper( get() ) } withOptions { createdAtStart() }
    single { SchwerteScraper( get() ) } withOptions { createdAtStart() }

}