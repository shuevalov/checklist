package ru.shuevalov.checklist.ui.screens.checklist

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.shuevalov.checklist.data.model.Task
import ru.shuevalov.checklist.data.model.TaskCategory
import ru.shuevalov.checklist.data.model.TaskCategoryInfo
import ru.shuevalov.checklist.ui.screens.task.TaskUiState
import ru.shuevalov.checklist.ui.theme.ChecklistTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistScreen(
    viewModel: ChecklistViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ChecklistContent(
        uiState = uiState,
        onDeleteClick = viewModel::deleteTask,
        updateTask = viewModel::updateTask,
        addTask = viewModel::insertTask
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistContent(
    modifier: Modifier = Modifier,
    uiState: ChecklistUiState,
    onDeleteClick: (Task) -> Unit,
    updateTask: (Task) -> Unit,
    addTask: (Task) -> Unit,

) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("my tasks") },
                navigationIcon = { Icons.Filled.MoreVert }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true }
            ) {
                Icon(Icons.Filled.Add, "add task")
            }
        }
    ) { padding ->
        ChecklistBody(
            taskList = uiState.tasks,
            contentPadding = padding,
            onDeleteClick = onDeleteClick,
            updateTask = updateTask
        )

        if (showBottomSheet) {
            TaskSheet(
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false },
                addTask = addTask
            )
        }
    }
}

@Composable
fun ChecklistBody(
    modifier: Modifier = Modifier,
    taskList: List<Task> = emptyList(),
    onDeleteClick: (Task) -> Unit,
    updateTask: (Task) -> Unit,
    contentPadding: PaddingValues
) {

    LazyColumn(
        modifier = modifier
            .padding(contentPadding)
            .fillMaxSize()
    ) {
        items(taskList) { task ->
            TaskCard(
                task = task,
                onCheckedChange = { updateTask(task.copy(isCompleted = !task.isCompleted)) },
                onDeleteClick = { onDeleteClick(task) }
            )
        }
    }


    // todo

}

@Composable
fun TaskCard(
    task: Task,
    onCheckedChange: ((Boolean) -> Unit)?,
    onDeleteClick: () -> Unit,
) {
    Box (
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Canvas(
                modifier = Modifier.size(16.dp, height = 40.dp)
            ) {
                val color = TaskCategoryInfo(task.category).color
                drawRect(
                    color = color,
                    size = size
                )
            }
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCheckedChange
            )
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = task.title
            )
        }
        IconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = onDeleteClick
        ) {
            Icon(Icons.Filled.Clear, "delete task")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    addTask: (Task) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(TaskCategory.DEFAULT) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("make task")
            TextField(
                value = text,
                onValueChange = {
                    text = it
                }
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(TaskCategory.entries.toTypedArray()) { category ->
                    val info = TaskCategoryInfo(category)
                    var selected by remember { mutableStateOf(false) }

                    FilterChip(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        onClick = {
                            selectedCategory = category
                            selected = !selected
                        },
                        selected = selected,
                        label = { Text(info.title) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = info.color,
                            labelColor = Color.White
                        )
                    )
                }
            }

            Button(
                onClick = {
                    addTask(Task(title = text, category = selectedCategory, isCompleted = false))
                    onDismissRequest()
                }
            ) {
                Text("add task")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChecklistPreview() {
    ChecklistTheme {
        val uiState = ChecklistUiState(listOf(Task(
            id = 2,
            category = TaskCategory.SHOP,
            title = "first task",
            isCompleted = false
        )))
        ChecklistContent(
            uiState = uiState,
            onDeleteClick = {},
            updateTask = {},
            addTask = {}
        )
    }
}
