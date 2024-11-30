package ru.shuevalov.checklist.ui.screens.checklist

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.shuevalov.checklist.data.model.Task
import ru.shuevalov.checklist.data.model.TaskCategory
import ru.shuevalov.checklist.ui.AppViewModelProvider
import ru.shuevalov.checklist.ui.screens.task.TaskUiState
import ru.shuevalov.checklist.ui.theme.ChecklistTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistScreen(
    viewModel: ChecklistViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    // val taskUiState
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
            addTask = showBottomSheet,
            contentPadding = padding
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                TaskSheet(
                    uiState = uiState.taskUiState,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun TaskSheet(
    uiState: TaskUiState,
    viewModel: ChecklistViewModel
) {
    var text by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("make task")
        TextField(
            value = text,
            onValueChange = { text = it }
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(TaskCategory.entries.toTypedArray()) {
//                FilterChip(
//                    selected = selected,
//                    label =
//                )
            }
        }
    }
}

@Composable
fun GetChipLabel(task: TaskCategory) {

}

@Composable
fun GetChipColor(task: TaskCategory) {

}

@Composable
fun ChecklistBody(
    modifier: Modifier = Modifier,
    taskList: List<TaskUiState> = emptyList(),
    addTask: Boolean,
    contentPadding: PaddingValues
) {

    LazyColumn(
        modifier = modifier
            .padding(contentPadding)
            .fillMaxSize()
    ) {
        items(taskList) {
            TaskCard(it) {

            }
        }
    }


    // todo

}

@Composable
fun TaskCard(
    task: TaskUiState,
    onCheckedChange: ((Boolean) -> Unit)?
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = onCheckedChange
        )
        Text(task.title)
    }
}

@Preview(showBackground = true)
@Composable
private fun ChecklistPreview() {
    ChecklistTheme {
        ChecklistScreen()
    }
}