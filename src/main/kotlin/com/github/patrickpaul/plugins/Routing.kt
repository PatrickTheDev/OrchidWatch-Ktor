package com.github.patrickpaul.plugins

import com.github.patrickpaul.data.product.ProductDataSource
import com.github.patrickpaul.data.product.productDAO
import com.github.patrickpaul.data.push.pushDAO
import com.github.patrickpaul.data.scraper.ScraperDataSource
import com.github.patrickpaul.data.scraper.scraperDAO
import com.github.patrickpaul.data.user.UserDataSource
import com.github.patrickpaul.plugins.routes.*
import com.github.patrickpaul.security.hashing.HashingService
import com.github.patrickpaul.security.token.TokenConfig
import com.github.patrickpaul.security.token.TokenService
import com.github.patrickpaul.util.getKoinInstance
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    firebaseMessaging: FirebaseMessaging,
) {
    routing {
        // TODO: securityRouting()

//        healthRouting(
//            scraperDao = scraperDAO,
//        )

//        productsRouting(
//            productDao = productDAO,
//        )

        pushRouting(firebaseMessaging)
    }
}

private fun Route.securityRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
) {
    signIn(userDataSource, hashingService, tokenService, tokenConfig)
    signUp(hashingService, userDataSource)
    authenticate()
    getSecretInfo()
    scraping()
}

private fun Route.healthRouting(
    scraperDao: ScraperDataSource,
) {
    route("/health") {
        get {
            val scrapers = scraperDao.allScrapers()
            call.respond(HttpStatusCode.OK, scrapers)
        }
    }
}

private fun Route.productsRouting(
    productDao: ProductDataSource
) {
    route("/products") {
        get {
            val products = productDao.allProducts()
            call.respond(HttpStatusCode.OK, products)
        }
        // TODO: get("{id}") { val id = call.parameters.getOrFail<Int>("id").toInt() }
        delete {
            val productsDeleted = productDao.deleteAllProducts()

            val statusCode =
                if (productsDeleted) HttpStatusCode.OK
                else HttpStatusCode.NoContent

            call.respond(statusCode)
        }
    }
}

private fun Route.pushRouting(
    firebaseMessaging: FirebaseMessaging,
) {
    route("/push") {
        post {
            call.request.queryParameters["token"]?.let { token ->
                pushDAO.putToken(token)
            }
        }
        get {
            val tokens = pushDAO.allTokens().map { it.value }
            val message = MulticastMessage.builder()
                .putData("value", "test")
                .addAllTokens(tokens)
                .build()

            firebaseMessaging.sendEachForMulticast(message)
        }
    }
}