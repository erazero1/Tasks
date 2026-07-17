package com.erazero1.tasks.data.model

import com.erazero1.tasks.domain.model.Todo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("userId")
    val userId: Int,
    @SerialName("title")
    val title: String?,
    @SerialName("completed")
    val isServerCompleted: Boolean?,
)

fun TodoDTO.toDomain(): Todo = Todo(
    id = this.id,
    userId = this.userId,
    title = this.title.orEmpty(),
    isServerCompleted = this.isServerCompleted == true
)
