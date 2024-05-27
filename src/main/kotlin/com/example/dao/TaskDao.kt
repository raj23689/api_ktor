package com.example.dao

import com.example.models.Task
import com.example.models.Tasks
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class TaskDao {
    private fun rowToTask(row: ResultRow) = Task(
        id = row[Tasks.id],
        title = row[Tasks.title],
        details = row[Tasks.details],
        status = row[Tasks.status],
        createdDate = row[Tasks.createdDate],
    )

    suspend fun getTasks() = dbQuery {
        Tasks.selectAll().map { rowToTask(it) }
    }

    suspend fun getTask(id: Int): Task? = dbQuery {
        Tasks
            .select(Tasks.id eq id)
            .map { rowToTask(it) }
            .singleOrNull()
    }

    suspend fun createTask(title: String, details: String) = dbQuery {
        val statement = Tasks.insert {
            it[Tasks.title] = title
            it[Tasks.details] = details
        }
    }
}