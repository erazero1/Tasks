package com.erazero1.tasks.ui.userList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erazero1.tasks.domain.model.User
import com.erazero1.tasks.domain.repository.UserRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class UserListViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val _networkState = MutableStateFlow<UserListUiState>(UserListUiState.Initial)
    private var isLoading = false

    val state: StateFlow<UserListUiState> = combine(
        _networkState,
        _users,
        _searchQuery.debounce(300)
    ) { networkState, users, query ->

        if (networkState is UserListUiState.Loading || networkState is UserListUiState.Error) {
            return@combine networkState
        }

        if (users.isEmpty()) {
            UserListUiState.Empty
        } else {
            val filteredUsers = if (query.isBlank()) {
                users
            } else {
                users.filter { user ->
                    user.name.contains(query, ignoreCase = true) ||
                            user.username.contains(query, ignoreCase = true) ||
                            user.email.contains(query, ignoreCase = true)
                }
            }

            if (filteredUsers.isEmpty()) UserListUiState.Empty else UserListUiState.Success(filteredUsers)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserListUiState.Initial
    )

    init {
        loadUserList()
    }

    fun loadUserList() {
        if (isLoading) return
        isLoading = true

        viewModelScope.launch {
            _networkState.update { UserListUiState.Loading }

            userRepository.getUserList()
                .onSuccess { usersList ->
                    _users.update { usersList }
                    _networkState.update { UserListUiState.Initial }
                }
                .onFailure { throwable ->
                    _networkState.update { UserListUiState.Error(throwable) }
                }

            isLoading = false
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}