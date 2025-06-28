package com.github.patrickpaul

import com.github.patrickpaul.data.DatabaseFactory
import com.github.patrickpaul.plugins.*
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module
import java.io.FileInputStream



private const val DEFAULT_PORT = 9999
private const val DEFAULT_HOST = "0.0.0.0"

@OptIn(DelicateCoroutinesApi::class)
fun main() {
    val serviceAccount =
        FileInputStream("/home/admin/serviceAccountKey.json")

    val options: FirebaseOptions = FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    val firebaseApp = FirebaseApp.initializeApp(options)
    val firebaseMessaging = FirebaseMessaging.getInstance(firebaseApp)

    runBlocking {
        launch(newSingleThreadContext("ServerScope")) {
            embeddedServer(
                Netty,
                port = DEFAULT_PORT,
                host = DEFAULT_HOST,
                module = { module(firebaseMessaging) },
            )
                .start(wait = true)
        }
    }
}

fun Application.module(
    firebaseMessaging: FirebaseMessaging,
) {
    DatabaseFactory.init()

    configureDI()
    configureSerialization()
    configureRouting(firebaseMessaging)
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