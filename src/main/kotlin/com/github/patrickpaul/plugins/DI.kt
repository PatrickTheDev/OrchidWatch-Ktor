package com.github.patrickpaul.plugins

import com.github.patrickpaul.data.product.ProductDAOFacadeImpl
import com.github.patrickpaul.data.product.ProductDataSource
import com.github.patrickpaul.data.scraper.ScraperDAOFacadeImpl
import com.github.patrickpaul.data.scraper.ScraperDataSource
import com.github.patrickpaul.data.user.UserDataSource
import com.github.patrickpaul.data.user.UserDataSourceImpl
import com.github.patrickpaul.scraping.scrapers.*
import com.github.patrickpaul.security.hashing.HashingService
import com.github.patrickpaul.security.hashing.SHA256HashingService
import com.github.patrickpaul.security.token.JwtTokenService
import com.github.patrickpaul.security.token.TokenService
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

    single { ScraperDAOFacadeImpl() as ScraperDataSource }
    single { ProductDAOFacadeImpl() as ProductDataSource }
    single { UserDataSourceImpl() as UserDataSource }
    single { JwtTokenService() as TokenService }
    single { SHA256HashingService() as HashingService }

    single { CramerScraper( get() ) } withOptions { createdAtStart() }
    single { HennisScraper( get() ) } withOptions { createdAtStart() }
    single { KopfScraper( get() ) } withOptions { createdAtStart() }
    single { OrchidHouseScraper( get() ) } withOptions { createdAtStart() }
    single { SchwerteScraper( get() ) } withOptions { createdAtStart() }
    single { WichmannScraper( get() ) } withOptions { createdAtStart() }
    single { WlodarczykScraper( get() ) } withOptions { createdAtStart() }
}