package com.erazero1.tasks.ui.todoList

import com.erazero1.tasks.domain.model.Todo

sealed interface TodoUiState {
    data object Initial : TodoUiState
    data object Loading : TodoUiState
    data object Empty : TodoUiState
    data class Error(val throwable: Throwable) : TodoUiState
    data class Success(
        val todos: List<Todo>,
        val currentFilter: TodoFilter
    ) : TodoUiState
}