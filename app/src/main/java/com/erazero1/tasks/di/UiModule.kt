package com.erazero1.tasks.di

import com.erazero1.tasks.ui.todoList.TodoListViewModel
import com.erazero1.tasks.ui.userList.UserListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::UserListViewModel)

    viewModel { (userId: Int) -> TodoListViewModel(todoRepository = get(), userId = userId) }
}