package ru.shuevalov.checklist.data.repository

import kotlinx.coroutines.flow.Flow
import ru.shuevalov.checklist.data.model.Task

interface TasksRepository {
    fun getAllTasks(): Flow<List<Task>>
    suspend fun insert(task: Task)
    suspend fun update(task: Task)
    suspend fun delete(task: Task)
}

