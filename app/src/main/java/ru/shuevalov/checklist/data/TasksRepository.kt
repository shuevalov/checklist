package ru.shuevalov.checklist.data

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.shuevalov.checklist.data.database.TaskDao
import ru.shuevalov.checklist.data.database.TaskDatabase
import ru.shuevalov.checklist.data.module.Task

interface TasksRepository {
    fun getAllTasks(): Flow<List<Task>>
    suspend fun insert(task: Task)
    suspend fun update(task: Task)
    suspend fun delete(task: Task)
}

class TaskRepositoryImpl(
    context: Context,
    private val backgroundDispatcher: CoroutineDispatcher
) : TasksRepository {

    private val taskDao: TaskDao



    init {
        val database: TaskDatabase? = TaskDatabase.getInstance(context)
        taskDao = database!!.taskDao()
    }


    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getTasks()
    }

    override suspend fun insert(task: Task) {
        withContext(backgroundDispatcher) {
            taskDao.insertTask(task)
        }
    }

    override suspend fun update(task: Task) {
        withContext(backgroundDispatcher) {
            taskDao.updateTask(task)
        }
    }

    override suspend fun delete(task: Task) {
        withContext(backgroundDispatcher) {
            taskDao.deleteTask(task)
        }
    }
}