package ru.shuevalov.checklist.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import ru.shuevalov.checklist.data.database.TaskDao
import ru.shuevalov.checklist.data.model.Task

class TaskRepositoryImpl(private val taskDao: TaskDao) : TasksRepository {

    override fun getAllTasks(): Flow<List<Task>> = taskDao.getTasks()

    override suspend fun insert(task: Task) {
        Log.d("TaskDao:insert", "before insert, task id: ${task.id}")
        taskDao.insertTask(task)
    }

    override suspend fun update(task: Task) = taskDao.updateTask(task)

    override suspend fun delete(task: Task) = taskDao.deleteTask(task)
}