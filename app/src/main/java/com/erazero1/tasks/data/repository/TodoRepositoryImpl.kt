package com.erazero1.tasks.data.repository

import com.erazero1.tasks.data.api.TodoApi
import com.erazero1.tasks.data.model.toDomain
import com.erazero1.tasks.domain.model.Todo
import com.erazero1.tasks.domain.repository.TodoRepository
import com.erazero1.tasks.utils.mapToAppNetworkException
import kotlinx.coroutines.CancellationException

class TodoRepositoryImpl(
    private val api: TodoApi
) : TodoRepository {
    override suspend fun getTodoListByUserId(userId: Int): Result<List<Todo>> {
        return try {
            val todos = api.getTodoListByUserId(userId).map { it.toDomain() }
            Result.success(todos)
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            Result.failure(mapToAppNetworkException(e))
        }
    }
}