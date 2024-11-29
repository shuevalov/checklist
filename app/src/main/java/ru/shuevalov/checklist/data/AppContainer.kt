package ru.shuevalov.checklist.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import ru.shuevalov.checklist.data.repository.TaskRepositoryImpl
import ru.shuevalov.checklist.data.repository.TasksRepository

interface AppContainer {
    val tasksRepository: TasksRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val tasksRepository: TasksRepository by lazy {
        TaskRepositoryImpl(context, Dispatchers.Unconfined)
    }
}