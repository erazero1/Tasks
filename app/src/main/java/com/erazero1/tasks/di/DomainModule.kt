package com.erazero1.tasks.di

import com.erazero1.tasks.data.api.UserApi
import com.erazero1.tasks.data.repository.UserRepositoryImpl
import com.erazero1.tasks.domain.repository.UserRepository
import org.koin.dsl.module

val domainModule = module {
    single<UserRepository> {
        UserRepositoryImpl(get<UserApi>())
    }
}