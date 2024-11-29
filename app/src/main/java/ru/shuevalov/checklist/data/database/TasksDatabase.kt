package ru.shuevalov.checklist.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import ru.shuevalov.checklist.data.module.TaskCategory
import ru.shuevalov.checklist.data.module.Task

const val DATABASE_VERSION_CODE = 1

@TypeConverters()
@Database(entities = [Task::class], version = DATABASE_VERSION_CODE, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {

        @Volatile
        private var INSTANCE: TaskDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): TaskDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        TaskDatabase::class.java,
                        "best_todo_database"
                    ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
                }
            }
            return INSTANCE
        }

        val PREPOPULATE_DATA = listOf(
            Task(
                id = 1,
                title = "read 'kotlin in action' book",
                isCompleted = false,
                category = TaskCategory.STUDY
            ),
            Task(
                id = 2,
                title = "buy crash-ride",
                isCompleted = false,
                category = TaskCategory.PERSONAL
            )
        )
    }

}