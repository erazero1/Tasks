package com.erazero1.tasks.domain.repository

import com.erazero1.tasks.domain.model.Todo

interface TodoRepository {
    suspend fun getTodoListByUserId(userId: Int): Result<List<Todo>>
}