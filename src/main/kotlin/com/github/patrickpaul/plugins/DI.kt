package com.github.patrickpaul.plugins

import com.github.patrickpaul.scraping.CramerScraper
import com.github.patrickpaul.scraping.SchwerteScraper
import com.microsoft.playwright.Playwright
import io.ktor.server.application.*
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

fun Application.configureDI() {
    install(Koin) {
        SLF4JLogger()
        modules(mainModule)
    }
}

val mainModule = module {
    single { Playwright.create().firefox().launch() } withOptions { createdAtStart() }

    single { CramerScraper( get() ) } withOptions { createdAtStart() }
    single { SchwerteScraper( get() ) } withOptions { createdAtStart() }
}