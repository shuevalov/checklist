package ru.shuevalov.checklist.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.shuevalov.checklist.ui.screens.checklist.ChecklistViewModel

val viewModelModule = module {
    viewModelOf(::ChecklistViewModel)
}