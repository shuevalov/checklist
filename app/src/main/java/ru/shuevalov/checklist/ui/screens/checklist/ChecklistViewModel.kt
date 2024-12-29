package ru.shuevalov.checklist.ui.screens.checklist

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.shuevalov.checklist.data.repository.TasksRepository
import ru.shuevalov.checklist.data.database.TaskDatabase
import ru.shuevalov.checklist.data.model.Task
import ru.shuevalov.checklist.data.model.TaskCategory
import ru.shuevalov.checklist.ui.screens.task.TaskUiState
import ru.shuevalov.checklist.ui.screens.task.toTask
import ru.shuevalov.checklist.ui.screens.task.toTaskUiState

class ChecklistViewModel(
    private val repository: TasksRepository
) : ViewModel() {

    private val _dataLoading = MutableStateFlow<Boolean>(false)
    val dataLoading = _dataLoading.asStateFlow()

    init {
        for (task in TaskDatabase.PREPOPULATE_DATA) {
            viewModelScope.launch {
                repository.insert(task)
            }
        }
    }

    private fun <T> Flow<T>.mutableStateIn(
        scope: CoroutineScope,
        initialValue: T
    ): MutableStateFlow<T> {
        val flow = MutableStateFlow(initialValue)

        scope.launch {
            this@mutableStateIn.collect(flow)
        }

        return flow
    }


    @SuppressLint("BuildListAdds")
    private val _uiState: MutableStateFlow<ChecklistUiState> =
        repository.getAllTasks().map { list ->
            ChecklistUiState(
                buildList { repeat(list.size) { list.forEach { it.toTaskUiState() } } },
                TaskUiState()
            )
        }.mutableStateIn(
            scope = viewModelScope,
            initialValue = ChecklistUiState(taskUiState = TaskUiState())
        )

    val uiState = _uiState.asStateFlow()


    fun insertTask() {
        if (validateInput(uiState.value)) {
            doWithProgress {
                viewModelScope.launch {
                    repository.insert(_uiState.value.taskUiState.toTask())
                }
            }
        }
    }

    fun updateTaskUiState(
        text: String,
        category: TaskCategory
    ) {
        _uiState.update {
            Log.d("ChecklistViewModel:updateTaskUiState", "before copy")
            it.copy(
                taskUiState = TaskUiState(
                    title = text,
                    category = category
                )
            )
        }
    }

    fun updateTask() = doWithProgress {
        viewModelScope.launch {
            repository.update(uiState.value.taskUiState.toTask())
        }
    }

    fun deleteTask() = doWithProgress {
        viewModelScope.launch {
            repository.delete(uiState.value.taskUiState.toTask())
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

    private fun validateInput(uiState: ChecklistUiState): Boolean = with(uiState) {
        taskUiState.title.isNotBlank()
    }

    private fun showProgress() {
        _dataLoading.value = true
    }

    private fun hideProgress() {
        _dataLoading.value = false
    }

}

data class ChecklistUiState(
    val tasks: List<TaskUiState> = emptyList(),
    val taskUiState: TaskUiState
)