package com.erazero1.tasks.data.repository

import com.erazero1.tasks.data.api.UserApi
import com.erazero1.tasks.data.model.toDomain
import com.erazero1.tasks.domain.model.AppNetworkException
import com.erazero1.tasks.domain.model.User
import com.erazero1.tasks.domain.repository.UserRepository
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

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

    private fun mapToAppNetworkException(e: Exception): AppNetworkException {
        return when (e) {
            is IOException -> AppNetworkException.NoInternetException()
            is HttpException -> AppNetworkException.ServerException(e.code())
            else -> AppNetworkException.UnknownNetworkException(e.localizedMessage)
        }
    }
}