package com.github.patrickpaul.plugins

import com.github.patrickpaul.dao.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    // Starting point for a Ktor app:
    routing {

        get("/") {
            call.respondText("Hello World!")
        }

        route("/products") {
            get {
                call.respond(dao.allProducts())
            }
            get("{id?}") {
                val id = call.parameters["id"]?.toInt() ?: return@get call.respondText(
                    "Missing id",
                    status = HttpStatusCode.BadRequest,
                )
                val product = dao.product(id) ?: return@get call.respondText(
                    "No product found with $id",
                    status = HttpStatusCode.NotFound,
                )
                call.respond(product)
            }
            post {
//                val customer = call.receive<Customer>()
//                customerStorage.add(customer)
//                call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
            }
            delete("{id?}") {
                val id = call.parameters["id"]?.toInt() ?: return@delete call.respond(
                    HttpStatusCode.BadRequest
                )
//                if (customerStorage.removeIf { it.id == id }) {
//                    call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
//                } else {
//                    call.respondText("Not Found", status = HttpStatusCode.NotFound)
//                }
            }
        }

    }

}