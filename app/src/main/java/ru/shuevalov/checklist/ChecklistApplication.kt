package ru.shuevalov.checklist

import android.app.Application
import ru.shuevalov.checklist.data.AppContainer
import ru.shuevalov.checklist.data.AppDataContainer

class ChecklistApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}