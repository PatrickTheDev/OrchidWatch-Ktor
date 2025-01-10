package com.github.patrickpaul.data

import com.github.patrickpaul.data.user.Users
import com.github.patrickpaul.data.product.Products
import com.github.patrickpaul.data.scraper.Scrapers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)

        transaction(database) {
            SchemaUtils.drop(Scrapers, Products, Users)
            SchemaUtils.create(Scrapers)
            SchemaUtils.create(Products)
            SchemaUtils.create(Users)
        }

        runBlocking {
            transaction {
                Scrapers.insert {
                    it[name] = "CramerScraper"
                    it[isHealthy] = true
                }
                Scrapers.insert {
                    it[name] = "HennisScraper"
                    it[isHealthy] = true
                }
                Scrapers.insert {
                    it[name] = "KopfScraper"
                    it[isHealthy] = false
                }
                Scrapers.insert {
                    it[name] = "OrchidHouseScraper"
                    it[isHealthy] = true
                }
                Scrapers.insert {
                    it[name] = "SchwerteScraper"
                    it[isHealthy] = true
                }
                Scrapers.insert {
                    it[name] = "WichmannScraper"
                    it[isHealthy] = true
                }
                Scrapers.insert {
                    it[name] = "WlodarczykScraper"
                    it[isHealthy] = true
                }
            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}