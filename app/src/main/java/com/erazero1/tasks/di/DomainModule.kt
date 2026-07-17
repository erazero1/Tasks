package com.erazero1.tasks.di

import com.erazero1.tasks.data.api.TodoApi
import com.erazero1.tasks.data.api.UserApi
import com.erazero1.tasks.data.repository.TodoRepositoryImpl
import com.erazero1.tasks.data.repository.UserRepositoryImpl
import com.erazero1.tasks.domain.repository.TodoRepository
import com.erazero1.tasks.domain.repository.UserRepository
import org.koin.dsl.module

val domainModule = module {
    single<UserRepository> {
        UserRepositoryImpl(get<UserApi>())
    }

    single<TodoRepository> {
        TodoRepositoryImpl(get<TodoApi>())
    }
}