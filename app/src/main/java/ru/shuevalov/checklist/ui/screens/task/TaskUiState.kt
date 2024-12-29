package ru.shuevalov.checklist.ui.screens.task

import android.util.Log
import ru.shuevalov.checklist.data.model.TaskCategory
import ru.shuevalov.checklist.data.model.Task

data class TaskUiState(
    val id: Int = 0,
    val title: String = "",
    val isCompleted: Boolean = false,
    val category: TaskCategory = TaskCategory.DEFAULT
)

fun Task.toTaskUiState(): TaskUiState = TaskUiState(
    id = this.id,
    title = this.title,
    isCompleted = this.isCompleted,
    category = this.category
)

fun TaskUiState.toTask(): Task {
        Log.d("TaskUiState:toTask", "task title: ${this.title}")
        return Task(
            id = this.id,
            title = this.title,
            isCompleted = this.isCompleted,
            category = this.category
        )
    }
