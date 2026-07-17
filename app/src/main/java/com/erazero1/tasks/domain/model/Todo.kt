package com.erazero1.tasks.domain.model

enum class TodoLocalStatus {
    IN_PROGRESS, COMPLETED;
}

data class Todo(
    val id: Int,
    val userId: Int,
    val title: String,
    val isServerCompleted: Boolean,
    val localStatus: TodoLocalStatus = if (isServerCompleted) TodoLocalStatus.COMPLETED else TodoLocalStatus.IN_PROGRESS
)