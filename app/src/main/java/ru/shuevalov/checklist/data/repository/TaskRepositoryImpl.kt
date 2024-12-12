package ru.shuevalov.checklist.data.repository

import kotlinx.coroutines.flow.Flow
import ru.shuevalov.checklist.data.database.TaskDao
import ru.shuevalov.checklist.data.model.Task

class TaskRepositoryImpl(private val taskDao: TaskDao) : TasksRepository {

    override fun getAllTasks(): Flow<List<Task>> = taskDao.getTasks()

    override suspend fun insert(task: Task) = taskDao.insertTask(task)

    override suspend fun update(task: Task) = taskDao.updateTask(task)

    override suspend fun delete(task: Task) = taskDao.deleteTask(task)
}