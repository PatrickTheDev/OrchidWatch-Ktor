package com.github.patrickpaul.plugins.routes

import com.github.patrickpaul.data.product.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.scraping() {
    authenticate {
        route("/products") {
            get {
                call.respond(dao.allProducts())
            }
            delete {
                call.respond(dao.deleteAllProducts())
            }

            get("/{id?}") {
                val id = call.parameters["id"]?.toInt() ?: return@get call.respondText(
                    "Missing id",
                    status = HttpStatusCode.BadRequest,
                )
                val product = dao.productById(id) ?: return@get call.respondText(
                    "No product found with $id",
                    status = HttpStatusCode.NotFound,
                )
                call.respond(product)
            }
            get("/count") {
                call.respond(dao.allProducts().size)
            }
            get("/diff") {
                val parameters = call.receiveParameters()
                val regex = Regex("[0123456789,]+")
                val clientIdsString = parameters["ids"]
                    ?.removePrefix("[")
                    ?.removeSuffix("]")
                val onlyComma = clientIdsString?.toCharArray()?.all { it == ",".toCharArray().first() } ?: false
                if (clientIdsString.isNullOrBlank()
                    || !clientIdsString.matches(regex)
                    || onlyComma
                ) {
                    return@get call.respondText(
                        "Missing ids or wrong parameters!",
                        status = HttpStatusCode.BadRequest,
                    )
                }
                val clientIds = clientIdsString
                    .split(",")
                    .mapNotNull { it.toIntOrNull() }
                val serverIds = dao
                    .allProductIds()
                    .toMutableList()
                val diff = (clientIds + serverIds)
                    .groupBy { it }
                    .filter { it.value.size == 1 }
                    .flatMap { it.value }
                val products = diff.mapNotNull { dao.productById(it) }

                call.respond(products)
            }
            get("/ids") {
                call.respond(dao.allProductIds())
            }
        }
    }
}