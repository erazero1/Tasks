package com.erazero1.tasks.data.repository

import com.erazero1.tasks.data.api.UserApi
import com.erazero1.tasks.data.model.toDomain
import com.erazero1.tasks.domain.model.User
import com.erazero1.tasks.domain.repository.UserRepository

class UserRepositoryImpl(
    private val api: UserApi
) : UserRepository {
    override suspend fun getUserList(): Result<List<User>> {
        return api.getUserList().map { userList -> userList.map { user -> user.toDomain() } }
    }

    override suspend fun getUserById(userId: Int): Result<User> {
        return api.getUserById(userId).map { user -> user.toDomain() }
    }
}