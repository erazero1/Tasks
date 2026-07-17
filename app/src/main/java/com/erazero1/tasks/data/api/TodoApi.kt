package com.erazero1.tasks.data.api

import com.erazero1.tasks.data.model.TodoDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface TodoApi {
    @GET("/todos")
    suspend fun getTodoListByUserId(
        @Query("userId") userId: Int,
    ): List<TodoDTO>
}