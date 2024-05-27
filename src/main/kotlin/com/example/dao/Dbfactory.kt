package com.example.dao

import com.example.models.Tasks
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


fun hikari(url: String, user: String, pass: String, pool: Int): HikariDataSource {
    val config = HikariConfig()
    config.driverClassName = "org.postgresql.Driver"
    config.jdbcUrl = url
    config.username = user
    config.password = pass
    config.maximumPoolSize = pool
    config.isAutoCommit = false
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    return HikariDataSource(config)
}

object DbFactory {
    fun init(environment: ApplicationEnvironment) {
        val url = environment.config.property("db.url").getString()
        val user = environment.config.property("db.user").getString()
        val pass = environment.config.property("db.pass").getString()
        val pool = environment.config.property("db.pool").getString().toInt()

        Database.connect(hikari(url, user, pass, pool))
        transaction {
            SchemaUtils.create(Tasks)
        }
    }
}

suspend fun <T> dbQuery(
    block: () -> T
): T = withContext(Dispatchers.IO) {
    transaction { block() }
}