package com.erazero1.tasks.di

import com.erazero1.tasks.ui.userList.UserListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::UserListViewModel)
}