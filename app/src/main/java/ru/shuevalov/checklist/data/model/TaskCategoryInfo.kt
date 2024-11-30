package ru.shuevalov.checklist.data.model

import androidx.compose.ui.graphics.Color

class TaskCategoryInfo(val category: TaskCategory) {
    val color: Color = when (category) {
        TaskCategory.WORK -> Color.Red
        TaskCategory.SHOP -> Color.Yellow
        TaskCategory.FITNESS -> Color.Green
        TaskCategory.STUDY -> Color.Magenta
        TaskCategory.PERSONAL -> Color.Cyan
        else -> Color.White
    }

    val title: String = when (category) {
        TaskCategory.WORK -> "work"
        TaskCategory.SHOP -> "shop"
        TaskCategory.FITNESS -> "fitness"
        TaskCategory.STUDY -> "study"
        TaskCategory.PERSONAL -> "personal"
        else -> ""
    }
}