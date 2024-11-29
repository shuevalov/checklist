package ru.shuevalov.checklist.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.shuevalov.checklist.ChecklistApplication
import ru.shuevalov.checklist.ui.screens.tasklist.TaskListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        //todo: task vm
        initializer {
            TaskListViewModel(ChecklistApplication().container.taskRepository)
        }
    }
}