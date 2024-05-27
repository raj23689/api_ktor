package dev.com.lazy.api.routes

import dev.com.lazy.api.dao.TaskDao
import dev.com.lazy.api.models.TaskDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.task(
    taskDao: TaskDao,
) {
    route("/api/v1/tasks") {
        get {
            call.respond(taskDao.getTasks())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalStateException("Numeric ID required")
            val task = taskDao.getTask(id)
            if (task != null) {
                call.respond(task)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        post {
            val dto = call.receive<TaskDto>()
            taskDao.createTask(dto.title ?: "", dto.details ?: "")
            call.respond(HttpStatusCode.Created)
        }


    }
}