package com.erazero1.tasks.ui.userList

import com.erazero1.tasks.domain.model.User

sealed interface UserListUiState {
    data object Initial : UserListUiState
    data object Loading : UserListUiState
    data object Empty : UserListUiState

    data class Error(val throwable: Throwable) : UserListUiState
    data class Success(val users: List<User>) : UserListUiState
}