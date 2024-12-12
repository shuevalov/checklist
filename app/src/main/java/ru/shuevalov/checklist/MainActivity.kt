package ru.shuevalov.checklist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.shuevalov.checklist.ui.screens.checklist.ChecklistScreen
import ru.shuevalov.checklist.ui.theme.ChecklistTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChecklistTheme {
                ChecklistScreen()
            }
        }
    }
}
