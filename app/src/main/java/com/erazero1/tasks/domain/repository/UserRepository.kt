package com.erazero1.tasks.domain.repository

import com.erazero1.tasks.domain.model.User

interface UserRepository {
    suspend fun getUserList(): Result<List<User>>
    suspend fun getUserById(userId: Int): Result<User>
}