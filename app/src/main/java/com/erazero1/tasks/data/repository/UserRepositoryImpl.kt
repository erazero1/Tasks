package com.erazero1.tasks.data.repository

import com.erazero1.tasks.data.api.UserApi
import com.erazero1.tasks.data.model.toDomain
import com.erazero1.tasks.domain.model.User
import com.erazero1.tasks.domain.repository.UserRepository
import com.erazero1.tasks.utils.mapToAppNetworkException
import kotlinx.coroutines.CancellationException

class UserRepositoryImpl(
    private val api: UserApi
) : UserRepository {
    override suspend fun getUserList(): Result<List<User>> {
        return try {
            val users = api.getUserList().map { it.toDomain() }
            Result.success(users)
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            Result.failure(mapToAppNetworkException(e))
        }
    }

    override suspend fun getUserById(userId: Int): Result<User> {
        return try {
            val user = api.getUserById(userId).toDomain()
            Result.success(user)
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            Result.failure(mapToAppNetworkException(e))
        }
    }
}