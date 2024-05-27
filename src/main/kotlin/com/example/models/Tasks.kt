package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Tasks: Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val details = text("details")
    val status = enumerationByName<TaskStatus>("status", 10)
    val createdDate = long("created_date")

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class Task (
    val id: Int,
    val title: String,
    val details: String,
    val status: TaskStatus,
    val createdDate: Long
)

@Serializable
data class TaskDto (
    val id: Int?,
    val title: String?,
    val details: String?,
    val status: TaskStatus?,
)

enum class TaskStatus(status: String) {
    TODO("todo"),
    InProgress("in-progress"),
    Done("done");
}