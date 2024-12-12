package ru.shuevalov.checklist.di

import org.koin.dsl.module
import ru.shuevalov.checklist.data.repository.TaskRepositoryImpl
import ru.shuevalov.checklist.data.repository.TasksRepository

val repositoryModule = module {
    single<TasksRepository> { TaskRepositoryImpl(get()) }
}