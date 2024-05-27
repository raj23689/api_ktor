package dev.com.lazy.api

import dev.com.lazy.api.dao.DbFactory
import dev.com.lazy.api.dao.TaskDao
import dev.com.lazy.api.routes.task
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
   install(DefaultHeaders)

    @OptIn(ExperimentalSerializationApi::class)
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            explicitNulls = true
        })
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }

    DbFactory.init(environment)
    val taskDao = TaskDao()

    install(Routing) {
        task(taskDao)
    }
}
