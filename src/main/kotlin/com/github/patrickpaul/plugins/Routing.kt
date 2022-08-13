package com.github.patrickpaul.plugins

import com.github.patrickpaul.dao.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {

        get("/") {
            call.respondText("Hello World!")
        }

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
                // TODO
            }
        }

    }

}