package com.erazero1.tasks.data.api

import com.erazero1.tasks.data.model.UserDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("/users")
    suspend fun getUserList(): Result<List<UserDTO>>

    @GET("/users/{userId}")
    suspend fun getUserById(@Path("userId") userId: Int): Result<UserDTO>
}