package ru.shuevalov.checklist.ui.screens.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.shuevalov.checklist.data.repository.TasksRepository
import ru.shuevalov.checklist.data.database.TaskDatabase
import ru.shuevalov.checklist.data.model.Task
import ru.shuevalov.checklist.ui.screens.task.TaskUiState
import ru.shuevalov.checklist.ui.screens.task.toTask

class TaskListViewModel(private val repository: TasksRepository) : ViewModel() {

    private val _dataLoading = MutableStateFlow<Boolean>(false)
    val dataLoading = _dataLoading.asStateFlow()

    // todo: separate vm for task
    private val _taskUiState = MutableStateFlow(TaskUiState())
    val taskUiState = _taskUiState.asStateFlow()


    val uiState: StateFlow<TaskListUiState> =
        repository.getAllTasks().map { TaskListUiState(it) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = TaskListUiState()
        )

    init {
        for (task in TaskDatabase.PREPOPULATE_DATA) {
            viewModelScope.launch {
                repository.insert(task)
            }
        }

    }

    fun insertTask() {
        if (validateInput(_taskUiState.value)) {
            doWithProgress {
                viewModelScope.launch() {
                    repository.insert(_taskUiState.value.toTask())
                }
            }
        }
    }

    fun updateTask(task: Task) {
        doWithProgress {
            viewModelScope.launch {
                repository.update(_taskUiState.value.toTask())
            }
        }
    }

    fun deleteTask(task: Task) {
        doWithProgress {
            viewModelScope.launch {
                repository.delete(_taskUiState.value.toTask())
            }
        }
    }

    fun getAllTasks(): Flow<List<Task>> {
        _dataLoading.value = true
        val tasks = repository.getAllTasks()
        _dataLoading.value = false
        return tasks
    }

    private fun doWithProgress(func: () -> Unit) {
        showProgress()
        func()
        hideProgress()
    }

    private fun validateInput(uiState: TaskUiState): Boolean = with(uiState) {
        title.isNotBlank()
    }

    private fun showProgress() {
        _dataLoading.value = true
    }

    private fun hideProgress() {
        _dataLoading.value = false
    }

    data class TaskListUiState(
        val tasks: List<Task> = emptyList()
    )
}