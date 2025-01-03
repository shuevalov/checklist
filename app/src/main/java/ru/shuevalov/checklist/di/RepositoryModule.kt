package ru.shuevalov.checklist.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.shuevalov.checklist.data.repository.TaskRepositoryImpl
import ru.shuevalov.checklist.data.repository.TasksRepository

val repositoryModule = module {
    singleOf(::TaskRepositoryImpl) { bind<TasksRepository>() }
}