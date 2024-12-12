package ru.shuevalov.checklist.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.shuevalov.checklist.data.database.TaskDao
import ru.shuevalov.checklist.data.database.TaskDatabase

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            TaskDatabase::class.java,
            "best_task_database"
        ).fallbackToDestructiveMigration().build()
    }

    single { get<TaskDatabase>().taskDao() }
}